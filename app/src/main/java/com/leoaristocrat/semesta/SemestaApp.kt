package com.leoaristocrat.semesta

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoaristocrat.semesta.core.di.rememberSemestaEntryPoint
import com.leoaristocrat.semesta.core.design.theme.SemestaTheme
import com.leoaristocrat.semesta.core.navigation.RootNavGraph
import com.leoaristocrat.semesta.core.utils.GradingScaleUtils
import com.leoaristocrat.semesta.feature_user.domain.AppearancePreferences
import com.leoaristocrat.semesta.feature_user.domain.CustomThemeBase
import com.leoaristocrat.semesta.feature_user.domain.VisualPreference
import androidx.compose.runtime.getValue

@Composable
fun SemestaApp(
    modifier: Modifier = Modifier,
    launchRoute: String? = null,
    onLaunchRouteConsumed: () -> Unit = {},
    onDarkThemeChanged: (Boolean) -> Unit = {}
) {
    val entryPoint = rememberSemestaEntryPoint()
    val profile by entryPoint.userRepository().userProfile.collectAsStateWithLifecycle()
    val visualPreference = profile?.visualPreference ?: VisualPreference.SYSTEM
    val appearance = profile?.appearancePreferences ?: AppearancePreferences.defaults()
    val accessibility = profile?.accessibilityPreferences
        ?: com.leoaristocrat.semesta.feature_user.domain.AccessibilityPreferences()
    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (visualPreference) {
        VisualPreference.SYSTEM -> systemDark
        VisualPreference.LIGHT -> false
        VisualPreference.DARK -> true
        VisualPreference.OLED -> true
        VisualPreference.CUSTOM -> when (appearance.customThemeBase) {
            CustomThemeBase.SYSTEM -> systemDark
            CustomThemeBase.LIGHT -> false
            CustomThemeBase.DARK -> true
        }
    }

    SideEffect {
        onDarkThemeChanged(darkTheme)
        GradingScaleUtils.configureDecimalPlaces(appearance.decimalPlaces)
    }

    SemestaTheme(
        darkTheme = darkTheme,
        oledTheme = visualPreference == VisualPreference.OLED,
        appearance = appearance,
        accessibility = accessibility
    ) {
        RootNavGraph(
            modifier = modifier,
            launchRoute = launchRoute,
            onLaunchRouteConsumed = onLaunchRouteConsumed
        )
    }
}
