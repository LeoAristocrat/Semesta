package com.leoaristocrat.semesta.core.design.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoaristocrat.semesta.R
import com.leoaristocrat.semesta.core.design.theme.hayMovimiento

@Composable
fun SemestaAnimatedLaunchScreen(modifier: Modifier = Modifier, timeScale: Float = 1f, onAnimationFinished: () -> Unit) {
    val finish by rememberUpdatedState(onAnimationFinished)
    val motion = hayMovimiento()
    val opacity = remember { Animatable(if (motion) 0f else 1f) }
    LaunchedEffect(motion) {
        if (motion) opacity.animateTo(1f, tween((320 * timeScale.coerceIn(.2f, 1f)).toInt()))
        finish()
    }
    Column(modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).graphicsLayer { alpha = opacity.value },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)) {
        SemestaLogoMark(size = 88.dp)
        SemestaWordmark(fontSize = 32.sp)
        Text(stringResource(R.string.semesta_tagline), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
