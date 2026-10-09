// design-tokens-exempt: fixed official brand artwork; typography uses the active theme
package com.leoaristocrat.semesta.core.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoaristocrat.semesta.R

// design-tokens-ok: canonical brand seed; the UI mark uses the selected theme.
val BrandPurple = Color(0xFF514EC5)

/** Official graduation-cap / S-ribbon artwork, displayed in its original colors. */
@Composable
fun SemestaLogoMark(modifier: Modifier = Modifier, size: Dp = 32.dp) {
    Image(
        painter = painterResource(R.drawable.semesta_logo),
        contentDescription = stringResource(R.string.app_name),
        modifier = modifier.size(size),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun SemestaWordmark(modifier: Modifier = Modifier, fontSize: TextUnit = 20.sp, fontWeight: FontWeight = FontWeight.SemiBold) {
    Text(stringResource(R.string.app_name), modifier, color = MaterialTheme.colorScheme.onSurface,
        fontSize = fontSize, lineHeight = fontSize * 1.25f,
        fontWeight = fontWeight, letterSpacing = (-.4).sp)
}
