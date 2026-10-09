package com.leoaristocrat.semesta.core.di

import android.content.Context
import com.leoaristocrat.semesta.feature_updates.data.GitHubReleaseUpdateRepository
import com.leoaristocrat.semesta.feature_updates.domain.UpdateRepository
import com.leoaristocrat.semesta.feature_user.data.FirebaseGoogleAuthService
import com.leoaristocrat.semesta.feature_user.domain.AccountAuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServicesModule {

    @Provides
    @Singleton
    fun provideAccountAuthService(): AccountAuthService = FirebaseGoogleAuthService()

    @Provides
    @Singleton
    fun provideUpdateRepository(@ApplicationContext context: Context): UpdateRepository =
        GitHubReleaseUpdateRepository(context)
}
