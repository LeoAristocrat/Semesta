package com.leoaristocrat.semesta.feature_setup.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.leoaristocrat.semesta.core.design.components.SemestaLogoMark
import com.leoaristocrat.semesta.core.design.theme.hayMovimiento

@Composable
fun SetupFinishTransition(onFinished: () -> Unit, modifier: Modifier = Modifier) {
    val finish by rememberUpdatedState(onFinished)
    val motion = hayMovimiento()
    val opacity = remember { Animatable(1f) }
    LaunchedEffect(motion) {
        if (motion) opacity.animateTo(0f, tween(220))
        finish()
    }
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        SemestaLogoMark(Modifier.graphicsLayer { alpha = opacity.value }, 80.dp)
    }
}
