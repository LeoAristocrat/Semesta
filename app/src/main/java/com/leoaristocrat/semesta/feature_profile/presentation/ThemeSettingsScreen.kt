@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.leoaristocrat.semesta.feature_profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.selection.selectable
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import android.os.Build
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.Switch
import com.leoaristocrat.semesta.core.design.components.SemestaSearchField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.leoaristocrat.semesta.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoaristocrat.semesta.core.design.components.LargeTitleScaffold
import com.leoaristocrat.semesta.core.design.components.SemestaSegmentedControl
import com.leoaristocrat.semesta.core.design.components.SemestaSegmentedOption
import com.leoaristocrat.semesta.core.design.components.cleanClickable
import com.leoaristocrat.semesta.feature_user.domain.portraitUrl
import com.leoaristocrat.semesta.core.design.theme.AppTheme
import com.leoaristocrat.semesta.core.design.theme.AppThemes
import com.leoaristocrat.semesta.core.design.theme.LocalInterfaceSpacing
import com.leoaristocrat.semesta.core.design.theme.LocalIsDarkTheme
import com.leoaristocrat.semesta.core.design.theme.SectionLabelStyle
import com.leoaristocrat.semesta.core.design.theme.ThemePalette
import com.leoaristocrat.semesta.core.design.theme.scrollBottomRoom
import com.leoaristocrat.semesta.feature_user.domain.AccentIntensity
import com.leoaristocrat.semesta.core.utils.Textos

/**
 * El tema, entero: fondo, tarjetas, tinta y acento a la vez.
 *
 * Vive en su propia pantalla y no dentro de Apariencia porque veintiocho temas más el modo,
 * el acento y su intensidad no caben en una fila de un hub sin convertirla en un scroll
 * interminable.
 *
 * **Dos cosas que estuvieron rotas y se arreglan aquí:**
 *
 * 1. El modo —claro, oscuro, OLED— no hacía nada salvo en OLED. El tema pintaba una sola
 *    paleta encima, y como casi todos son oscuros, «Claro» quedaba tapado. Ahora cada tema
 *    tiene sus dos caras y el modo dice cuál.
 * 2. El carrusel volvía al principio cada vez que se entraba, así que con el tema veinte
 *    puesto había que arrastrar veinte tarjetas para verlo. Ahora abre en el que está elegido.
 *
 * Y una que se quita: **Monet ya no está**. Con veintiocho temas y un acento a medida, tomar
 * prestada la paleta del fondo de pantalla no añadía nada y sí quitaba —mientras estaba
 * encendido, los temas quedaban en pausa y media pantalla no pintaba.
 */
@Composable
fun ThemeSettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val spacing = LocalInterfaceSpacing.current
    val current = profile ?: return
    val appearance = current.appearancePreferences
    val oscuro = LocalIsDarkTheme.current
    var query by rememberSaveable { mutableStateOf("") }
    val filteredThemes = AppThemes.catalog.filter { query.isBlank() || it.name.contains(query, ignoreCase = true) || it.family.contains(query, ignoreCase = true) }
    val columns = if (LocalConfiguration.current.screenWidthDp >= 700) 3 else 2

    LargeTitleScaffold(
        title = stringResource(R.string.settings_theme_title),
        subtitle = stringResource(R.string.settings_theme_subtitle),
        onBackClick = onBackClick,
        modifier = modifier,
        horizontalPadding = spacing.screenHorizontal,
        topPadding = 8.dp,
        bottomPadding = scrollBottomRoom,
        itemSpacing = 12.dp
    ) {
        item {
            HomePreviewCard(
                name = current.preferredName,
                photoUrl = current.portraitUrl,
                appearance = appearance
            )
        }

        item {
            Text(
                stringResource(R.string.settings_theme_section_mode),
                style = SectionLabelStyle,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        item {
            SemestaSegmentedControl(
                // El modo personalizado no entra: no hay forma de configurarlo desde ninguna
                // pantalla, así que como quinto segmento sería un botón que no lleva a nada.
                selected = current.visualPreference.orSystem(),
                options = VisiblePreferences.map { SemestaSegmentedOption(value = it, label = it.label()) },
                onSelected = viewModel::updateVisualPreference,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text(
                text = current.visualPreference.themeDescription(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(stringResource(R.string.semesta_dynamic_color), style = MaterialTheme.typography.titleMedium)
                    Text(stringResource(R.string.semesta_dynamic_color_desc), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = appearance.dynamicColor, enabled = Build.VERSION.SDK_INT >= 31,
                    onCheckedChange = { value -> viewModel.updateAppearance { it.copy(dynamicColor = value) } })
            }
        }
        item {
            val tema = AppThemes.byId(appearance.themeId)
            Text(
                text = buildString {
                    append(tema.name)
                    if (tema.family.isNotEmpty()) append(" · ").append(tema.family)
                    append(stringResource(R.string.settings_theme_desc_suffix))
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Text(
                stringResource(R.string.settings_theme_section_accent),
                style = SectionLabelStyle,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        item {
            Text(
                text = stringResource(R.string.settings_theme_accent_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item {
            AccentRow(
                delTema = AppThemes.byId(appearance.themeId).palette(oscuro).accent,
                elegido = appearance.customAccentColor?.let(::Color),
                onElegir = { color ->
                    // Se guarda como ARGB entero, que es lo que lee `Color(Int)` al arrancar.
                    // Empaquetar el `value` de Compose daría otro número y el color volvería
                    // distinto del que se tocó.
                    viewModel.updateAppearance { it.copy(customAccentColor = color?.toArgb(), dynamicColor = false) }
                }
            )
        }

        item {
            Text(
                stringResource(R.string.settings_theme_section_intensity),
                style = SectionLabelStyle,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        item {
            SemestaSegmentedControl(
                selected = appearance.accentIntensity,
                options = AccentIntensity.entries.map { SemestaSegmentedOption(value = it, label = it.label()) },
                onSelected = { valor -> viewModel.updateAppearance { it.copy(accentIntensity = valor) } },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Text(
                text = stringResource(R.string.settings_theme_intensity_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        item {
            Text(
                stringResource(R.string.settings_theme_section_theme),
                style = SectionLabelStyle,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
        item {
            SemestaSearchField(query = query, onQueryChange = { query = it }, placeholder = stringResource(R.string.semesta_theme_search), modifier = Modifier.fillMaxWidth())
        }
        filteredThemes.chunked(columns).forEach { themes ->
            item(key = "themes-" + themes.joinToString { it.id }) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    themes.forEach { theme ->
                        ThemeCard(tema = theme, oscuro = oscuro, elegido = theme.id == AppThemes.byId(appearance.themeId).id,
                            modifier = Modifier.weight(1f).heightIn(min = 152.dp),
                            onClick = { viewModel.updateAppearance { it.copy(themeId = theme.id, customAccentColor = null, dynamicColor = false) } })
                    }
                    repeat(columns - themes.size) { Spacer(Modifier.weight(1f)) }
                }
            }
        }
        if (filteredThemes.isEmpty()) item {
            Text(stringResource(R.string.semesta_no_themes), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

    }
}

/**
 * Los doce acentos, más el que trae el tema.
 *
 * El primero no es un color fijo: es «el del tema», y se marca cuando no hay ninguno a medida
 * elegido. Sin él no había forma de volver atrás después de tocar un punto.
 */
@Composable
private fun AccentRow(
    delTema: Color,
    elegido: Color?,
    onElegir: (Color?) -> Unit
) {
    val labels = androidx.compose.ui.res.stringArrayResource(R.array.semesta_accent_names)
    androidx.compose.foundation.layout.FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        (listOf(null) + Acentos).forEachIndexed { index, color ->
            val real = color ?: delTema
            val selected = if (color == null) elegido == null else elegido == color
            Box(Modifier.size(48.dp).clip(CircleShape)
                .background(real)
                .border(if (selected) 2.dp else 0.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                .then(Modifier.selectable(selected = selected, role = androidx.compose.ui.semantics.Role.RadioButton,
                    onClick = { onElegir(color) }))
                .semantics { contentDescription = labels[index] }, contentAlignment = Alignment.Center) {
                if (selected) Icon(Icons.Rounded.Check, null,
                    tint = com.leoaristocrat.semesta.core.design.theme.contentColorOn(real), modifier = Modifier.size(20.dp))
            }
        }
    }
}

private fun Color.luminancia(): Float = 0.299f * red + 0.587f * green + 0.114f * blue

/** Once tonos que funcionan sobre fondo claro y oscuro sin cambiar de valor. */
private val Acentos = listOf(
    Color(0xFF7F77DD), Color(0xFF3F8FE0), Color(0xFF2FB4C9), Color(0xFF4FBFA6), Color(0xFF5FC96E), // design-tokens-ok: paleta de acentos y su contraste
    Color(0xFFE0A63C), Color(0xFFE8693A), Color(0xFFE2564F), Color(0xFFE062A8), Color(0xFFC08BE0), // design-tokens-ok: paleta de acentos y su contraste
    Color(0xFF8C93A8) // design-tokens-ok: paleta de acentos y su contraste
)

/**
 * La tarjeta de un tema, pintada como un trozo de app y no como tres barras.
 *
 * Enseña lo que de verdad cambia: el fondo, una tarjeta encima con su tinta, y el acento en
 * una pastilla. Con barras sueltas, dos temas del mismo tono se veían iguales aunque su
 * contraste entre fondo y tarjeta fuera muy distinto, que es lo que se nota al usarlos.
 *
 * La cara que se pinta es la del modo puesto: enseñar el Dracula oscuro mientras la app está
 * en claro sería mentir sobre lo que va a pasar al tocarlo.
 */
@Composable
private fun ThemeCard(
    tema: AppTheme,
    oscuro: Boolean,
    elegido: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val p: ThemePalette = tema.palette(oscuro)
    val anillo = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(p.background)
            /*
             * El anillo de elegido va con **el acento de la app**, no con el del tema.
             *
             * Con el del tema, cada tarjeta se marcaba de un color distinto y no se leia como
             * «este es el elegido» sino como un adorno mas del tema: con el acento verde puesto
             * y Macchiato elegido, el anillo salia morado y no se entendia por que.
             * «Elegido» es lo mismo en toda la app, y en toda la app se marca con el acento.
             */
            .border(
                width = if (elegido) 3.dp else 0.dp,
                color = if (elegido) anillo else Color.Transparent,
                shape = RoundedCornerShape(18.dp)
            )
            .cleanClickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            // La tarjeta de dentro, con su tinta: es el contraste que se juzga al elegir.
            Surface(
                shape = RoundedCornerShape(9.dp),
                color = p.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(p.ink)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.45f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(p.ink.copy(alpha = 0.45f))
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(15.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(p.accent)
                )
                Box(
                    modifier = Modifier
                        .size(15.dp)
                        .clip(CircleShape)
                        .background(p.accent.copy(alpha = 0.5f))
                )
                if (elegido) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = stringResource(R.string.settings_theme_selected),
                        tint = anillo,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Column {
                Text(
                    text = tema.name,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = p.ink,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tema.family.ifEmpty { "Semesta" },
                    style = MaterialTheme.typography.labelSmall,
                    color = p.ink.copy(alpha = 0.78f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

internal fun AccentIntensity.label(): String {
    return when (this) {
        AccentIntensity.SOFT -> Textos.get(R.string.settings_accent_soft)
        AccentIntensity.BALANCED -> Textos.get(R.string.settings_accent_balanced)
        AccentIntensity.VIBRANT -> Textos.get(R.string.settings_accent_vibrant)
    }
}
