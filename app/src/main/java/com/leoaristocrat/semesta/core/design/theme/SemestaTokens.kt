// design-tokens-exempt: centralized Semesta palette derivation and contrast mathematics
package com.leoaristocrat.semesta.core.design.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.leoaristocrat.semesta.feature_user.domain.BackgroundStyle
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/** Shared layout and interaction constraints. Colors remain semantic theme roles. */
object SemestaTokens {
    val SpaceXs = 4.dp
    val SpaceSm = 8.dp
    val SpaceMd = 12.dp
    val SpaceLg = 16.dp
    val SpaceXl = 24.dp
    val Space2xl = 32.dp
    val TouchTarget = 48.dp
    val Icon = 22.dp
    val RailWidth = 88.dp
    val ReadingWidth = 960.dp
    val WorkspaceWidth = 1200.dp
    const val RailBreakpoint = 600
    const val ColumnsBreakpoint = 840
    const val MotionMillis = 220
}

/** WCAG contrast in linear sRGB; used for custom accents as well as curated palettes. */
internal fun contrastRatio(a: Color, b: Color): Float {
    fun luminance(c: Color): Float {
        fun linear(v: Float) = if (v <= .04045f) v / 12.92f else ((v + .055f) / 1.055f).pow(2.4f)
        return .2126f * linear(c.red) + .7152f * linear(c.green) + .0722f * linear(c.blue)
    }
    val first = luminance(a)
    val second = luminance(b)
    return (max(first, second) + .05f) / (min(first, second) + .05f)
}

/** Keep the selected hue, adjusting its lightness only as much as readability requires. */
internal fun readableAccent(accent: Color, background: Color, surface: Color): Color {
    val opaque = accent.copy(alpha = 1f)
    val target = if (contrastRatio(Color.White, background) > contrastRatio(Color.Black, background)) Color.White else Color.Black
    return (0..100).asSequence().map { opaque.mezclaCon(target, it / 100f) }
        .firstOrNull { contrastRatio(it, background) >= 4.5f && contrastRatio(it, surface) >= 4.5f }
        ?: target
}

internal fun ThemePalette.withBackground(style: BackgroundStyle, custom: Int?, dark: Boolean): ThemePalette {
    val tone = when (style) {
        BackgroundStyle.DEFAULT -> return this
        BackgroundStyle.PURE -> if (dark) Color.Black else Color.White
        BackgroundStyle.COOL -> background.mezclaCon(Color(0xFF577EAD), .05f)
        BackgroundStyle.VIOLET -> background.mezclaCon(Color(0xFF8A72BC), .05f)
        BackgroundStyle.CUSTOM -> custom?.let { Color(it).copy(alpha = 1f) } ?: return this
    }
    val text = if (contrastRatio(ink, tone) >= 7f) ink else contentColorOn(tone)
    // Move surfaces away from the readable foreground. Around middle gray, moving toward
    // the ink can make it impossible for a single accent to pass on both surfaces.
    val surfaceTarget = if (contrastRatio(Color.White, tone) > contrastRatio(Color.Black, tone)) Color.Black else Color.White
    val adjustedSurface = if (style == BackgroundStyle.CUSTOM) tone.mezclaCon(surfaceTarget, .07f) else surface
    return copy(background = tone, surface = adjustedSurface, ink = text)
}
