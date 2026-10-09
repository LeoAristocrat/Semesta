package com.leoaristocrat.semesta.core.design.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.VectorConverter
import com.leoaristocrat.semesta.core.datastore.AppearancePreferencesJson
import com.leoaristocrat.semesta.feature_user.domain.AppearancePreferences
import com.leoaristocrat.semesta.feature_user.domain.BackgroundStyle
import com.leoaristocrat.semesta.feature_user.domain.SurfaceAppearance
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test

@org.junit.runner.RunWith(org.robolectric.RobolectricTestRunner::class)
@org.robolectric.annotation.Config(manifest = org.robolectric.annotation.Config.NONE, sdk = [34])
@OptIn(androidx.compose.material3.ExperimentalMaterial3ExpressiveApi::class)
class SemestaThemeTest {
    @Test fun materialMotionStopsWhenAnimationIsDisabled() {
        fun duration(preference: com.leoaristocrat.semesta.feature_user.domain.MotionPreference): Long {
            val spec = SemestaMotionScheme(preference).defaultSpatialSpec<Float>().vectorize(Float.VectorConverter)
            return spec.getDurationNanos(AnimationVector1D(0f), AnimationVector1D(1f), AnimationVector1D(0f))
        }
        val full = duration(com.leoaristocrat.semesta.feature_user.domain.MotionPreference.FULL)
        val reduced = duration(com.leoaristocrat.semesta.feature_user.domain.MotionPreference.REDUCED)
        assertTrue(full > reduced && reduced > 0)
        assertEquals(0L, duration(com.leoaristocrat.semesta.feature_user.domain.MotionPreference.NONE))
    }
    @Test fun curatedPalettesRemainReadableInBothModes() {
        assertEquals(37, AppThemes.catalog.size)
        assertEquals(AppThemes.catalog.size, AppThemes.catalog.map { it.id }.toSet().size)
        AppThemes.catalog.forEach { theme ->
            listOf(false, true).forEach { dark ->
                val palette = theme.palette(dark)
                assertTrue("${theme.id} text", contrastRatio(palette.ink, palette.background) >= 4.5f)
                val accent = readableAccent(palette.accent, palette.background, palette.surface)
                assertTrue("${theme.id} accent", contrastRatio(accent, palette.surface) >= 4.5f)
                assertTrue("${theme.id} button", contrastRatio(contentColorOn(accent), accent) >= 4.5f)
            }
        }
    }

    @Test fun customBackgroundAndVeryLightAccentCannotHideText() {
        listOf(Color.White, Color.Black, Color(0xFF777777)).forEach { background ->
            val palette = AppThemes.byId("semesta").light.withBackground(BackgroundStyle.CUSTOM, background.toArgb(), false)
            val accent = readableAccent(Color.Yellow, palette.background, palette.surface)
            assertTrue(contrastRatio(palette.ink, palette.surface) >= 4.5f)
            assertTrue(contrastRatio(accent, palette.surface) >= 4.5f)
            assertTrue(contrastRatio(accent, palette.background) >= 4.5f)
        }
    }

    @Test fun oldBackupsKeepTheirThemeAndGainSafeCustomizationDefaults() {
        val base = AppearancePreferences.defaults()
        val restored = AppearancePreferencesJson.decode(JSONObject().put("themeId", "unistack"), base)
        assertEquals(AppThemes.byId("semesta"), AppThemes.byId(restored.themeId))
        assertFalse(restored.dynamicColor)
        assertEquals(SurfaceAppearance.TONAL, restored.surfaceAppearance)
        val customized = base.copy(dynamicColor = true, surfaceAppearance = SurfaceAppearance.OUTLINED)
        assertEquals(customized, AppearancePreferencesJson.decode(AppearancePreferencesJson.encode(customized), base))
    }
}
