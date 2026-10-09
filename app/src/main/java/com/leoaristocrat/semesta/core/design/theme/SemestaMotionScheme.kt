package com.leoaristocrat.semesta.core.design.theme

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MotionScheme
import com.leoaristocrat.semesta.feature_user.domain.MotionPreference

/** Quiet, bounded motion for Material components, including disabled-motion preferences. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
internal class SemestaMotionScheme(private val preference: MotionPreference, private val speed: Float = 1f) : MotionScheme {
    private fun <T> spec(milliseconds: Int): FiniteAnimationSpec<T> {
        if (preference == MotionPreference.NONE || speed == 0f) return snap()
        val allowance = if (preference == MotionPreference.REDUCED) .4f else 1f
        return tween((milliseconds * allowance * speed).toInt().coerceAtLeast(1), easing = FastOutSlowInEasing)
    }
    override fun <T> defaultSpatialSpec(): FiniteAnimationSpec<T> = spec(SemestaTokens.MotionMillis)
    override fun <T> fastSpatialSpec(): FiniteAnimationSpec<T> = spec(150)
    override fun <T> slowSpatialSpec(): FiniteAnimationSpec<T> = spec(280)
    override fun <T> defaultEffectsSpec(): FiniteAnimationSpec<T> = spec(180)
    override fun <T> fastEffectsSpec(): FiniteAnimationSpec<T> = spec(100)
    override fun <T> slowEffectsSpec(): FiniteAnimationSpec<T> = spec(240)
}
