package com.leoaristocrat.semesta.core.design.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/** Keeps the setup animation contract while presenting the official logo once. */
object SemestaBrandMark {
    const val HeightRatio = 1f
    data class Pill(val left: Float = 0f, val top: Float = 0f, val width: Float = 1f, val height: Float = 1f)
    val Pills = listOf(Pill())
}

@Composable
fun SemestaBrandPill(pill: SemestaBrandMark.Pill, markWidth: Dp, modifier: Modifier = Modifier) {
    SemestaLogoMark(modifier, markWidth)
}
