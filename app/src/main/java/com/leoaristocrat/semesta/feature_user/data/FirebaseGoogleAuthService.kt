package com.leoaristocrat.semesta.feature_user.data

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.leoaristocrat.semesta.BuildConfig
import com.leoaristocrat.semesta.feature_user.domain.AccountAuthService
import com.leoaristocrat.semesta.feature_user.domain.AuthProvider
import com.leoaristocrat.semesta.feature_user.domain.LinkedAccount
import kotlinx.coroutines.tasks.await
import com.leoaristocrat.semesta.core.utils.Textos
import com.leoaristocrat.semesta.R

class FirebaseGoogleAuthService : AccountAuthService {

    override suspend fun signInWithGoogle(context: Context): Result<LinkedAccount> = runCatching {
        ensureFirebaseConfigured(context)
        val webClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        check(webClientId.isNotBlank()) {
            Textos.get(R.string.semesta_google_unconfigured)
        }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        val credential = try {
            CredentialManager.create(context)
                .getCredential(context = context, request = request)
                .credential
        } catch (exception: NoCredentialException) {
            throw IllegalStateException(Textos.get(R.string.auth_err_no_credentials), exception)
        }

        val googleCredential = when {
            credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                try {
                    GoogleIdTokenCredential.createFrom(credential.data)
                } catch (exception: GoogleIdTokenParsingException) {
                    throw IllegalStateException(Textos.get(R.string.auth_google_credential_unreadable), exception)
                }
            }
            else -> throw IllegalStateException(Textos.get(R.string.auth_google_credential_foreign))
        }

        val firebaseCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)
        val authResult = Firebase.auth.signInWithCredential(firebaseCredential).await()
        val user = authResult.user ?: error(Textos.get(R.string.auth_err_no_user))

        LinkedAccount(
            provider = AuthProvider.GOOGLE,
            providerUserId = user.uid,
            displayName = user.displayName ?: googleCredential.displayName,
            email = user.email ?: googleCredential.id,
            photoUrl = user.photoUrl?.toString() ?: googleCredential.profilePictureUri?.toString()
        )
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        Firebase.auth.signOut()
    }

    private fun ensureFirebaseConfigured(context: Context) {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        check(FirebaseApp.getApps(context).isNotEmpty()) {
            Textos.get(R.string.cloud_backup_no_config)
        }
    }
}
