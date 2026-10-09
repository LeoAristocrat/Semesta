package com.leoaristocrat.semesta.core.utils

import android.content.Context
import android.content.res.Configuration
import com.leoaristocrat.semesta.feature_user.domain.AppLanguage
import java.util.Locale

/** English is the product language, including when restoring older language preferences. */
object LocaleHelper {
    private const val PREFS_NAME = "app_locale_preferences"
    private const val KEY_LANGUAGE = "app_language"

    @Suppress("UNUSED_PARAMETER")
    fun getPersistedLanguage(context: Context): AppLanguage = AppLanguage.ENGLISH

    @Suppress("UNUSED_PARAMETER")
    fun persistLanguage(context: Context, language: AppLanguage) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_LANGUAGE, AppLanguage.ENGLISH.name).apply()
    }

    @Suppress("UNUSED_PARAMETER")
    fun applyLocale(context: Context, language: AppLanguage = getPersistedLanguage(context)): Context {
        Locale.setDefault(Locale.ENGLISH)
        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(Locale.ENGLISH)
        return context.createConfigurationContext(configuration)
    }
}
