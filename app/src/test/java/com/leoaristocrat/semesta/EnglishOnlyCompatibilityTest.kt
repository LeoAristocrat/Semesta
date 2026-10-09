package com.leoaristocrat.semesta

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.test.core.app.ApplicationProvider
import com.leoaristocrat.semesta.core.datastore.AccessibilityPreferencesJson
import com.leoaristocrat.semesta.core.utils.LocaleHelper
import com.leoaristocrat.semesta.core.utils.Textos
import com.leoaristocrat.semesta.feature_user.domain.AccessibilityPreferences
import com.leoaristocrat.semesta.feature_user.domain.AppLanguage
import com.leoaristocrat.semesta.feature_user.domain.CurrencyPreference
import com.leoaristocrat.semesta.feature_user.domain.DateFormatPreference
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Locale

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = Application::class, sdk = [34])
class EnglishOnlyCompatibilityTest {
    private lateinit var originalLocale: Locale

    @Before fun setUp() {
        originalLocale = Locale.getDefault()
        Locale.setDefault(Locale.forLanguageTag("es-ES"))
    }

    @After fun tearDown() {
        Locale.setDefault(originalLocale)
        TextosDePrueba.instalar()
    }

    @Test fun legacyLanguagePreferencesUseEnglishOnASpanishDevice() {
        val app = ApplicationProvider.getApplicationContext<Context>()
        val config = Configuration(app.resources.configuration).apply {
            setLocale(Locale.forLanguageTag("es-ES"))
        }
        val deviceContext = app.createConfigurationContext(config)
        for (legacy in AppLanguage.entries) {
            app.getSharedPreferences("app_locale_preferences", Context.MODE_PRIVATE)
                .edit().putString("app_language", legacy.name).commit()
            assertEquals(AppLanguage.ENGLISH, LocaleHelper.getPersistedLanguage(app))
            val localized = LocaleHelper.applyLocale(deviceContext, legacy)
            assertEquals("en", localized.resources.configuration.locales[0].language)
            assertEquals("Settings", localized.getString(R.string.nav_settings))
            assertEquals("en", Locale.getDefault().language)
        }
    }

    @Test fun backgroundResourcesUseEnglishBeforeAnActivityStarts() {
        val app = ApplicationProvider.getApplicationContext<Context>()
        app.getSharedPreferences("app_locale_preferences", Context.MODE_PRIVATE)
            .edit().putString("app_language", "SPANISH").commit()
        Textos.desde(app)
        assertEquals("Settings", Textos.get(R.string.nav_settings))
        assertEquals("changelog_en.md", Textos.get(R.string.changelog_asset_name))
    }

    @Test fun highlightedOnboardingTextKeepsSpacesBetweenWords() {
        val context = LocaleHelper.applyLocale(ApplicationProvider.getApplicationContext<Context>())
        assertEquals("What is your name?",
            context.getString(R.string.setup_name_title_lead) +
                context.getString(R.string.setup_name_title_accent))
        assertEquals("All set, Arjun.", context.getString(R.string.setup_done_lead) + "Arjun.")
        assertEquals("You can always enable or disable modules\nfrom Settings later.",
            context.getString(R.string.setup_modules_footer_lead) +
                context.getString(R.string.setup_modules_footer_accent) +
                context.getString(R.string.setup_modules_footer_tail))
    }

    @Test fun legacyBackupMigrationPreservesOtherAccessibilityPreferences() {
        val original = AccessibilityPreferences(
            appLanguage = AppLanguage.SPANISH,
            use24HourTime = false,
            currency = CurrencyPreference.USD,
            dateFormat = DateFormatPreference.YMD,
            highContrastEnabled = true,
            boldText = true,
            hapticsEnabled = false
        )
        val json = AccessibilityPreferencesJson.encode(original)
        for (legacy in listOf("SPANISH", "SYSTEM", "ENGLISH", "unknown")) {
            json.put("appLanguage", legacy)
            val restored = AccessibilityPreferencesJson.decode(json, AccessibilityPreferences())
            assertEquals(original.copy(appLanguage = AppLanguage.ENGLISH), restored)
            assertEquals("ENGLISH", AccessibilityPreferencesJson.encode(restored).getString("appLanguage"))
        }
        assertEquals(AppLanguage.ENGLISH,
            AccessibilityPreferencesJson.decode(JSONObject(), original).appLanguage)
    }
}
