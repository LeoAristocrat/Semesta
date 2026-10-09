@file:OptIn(ExperimentalMaterial3ExpressiveApi::class, androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.leoaristocrat.semesta.feature_grades.presentation

import androidx.compose.material3.Icon
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.getValue
import com.leoaristocrat.semesta.core.design.theme.tweenDeMovimiento
import com.leoaristocrat.semesta.feature_user.domain.BadgeShape
import com.leoaristocrat.semesta.core.design.theme.LocalAccessibilityPreferences
import androidx.compose.foundation.background
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.togetherWith
import androidx.compose.animation.scaleOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.fadeIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import com.leoaristocrat.semesta.core.design.components.EvaluationBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.leoaristocrat.semesta.R
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.star
import androidx.compose.material3.toPath
import com.leoaristocrat.semesta.core.design.theme.LocalSectionColors
import com.leoaristocrat.semesta.core.design.theme.SectionLabelStyle
import com.leoaristocrat.semesta.core.utils.GradingScaleUtils
import com.leoaristocrat.semesta.core.utils.GradeAlertLevel
import com.leoaristocrat.semesta.core.utils.SubjectGradeCalculation
import com.leoaristocrat.semesta.core.utils.TargetOutlook
import com.leoaristocrat.semesta.feature_grades.domain.Subject
import com.leoaristocrat.semesta.feature_user.domain.GradingScale
import com.leoaristocrat.semesta.feature_schedule.domain.ClassSession

/**
 * Una materia en la lista.
 *
 * Se lee en diagonal: quién, cuánto lleva evaluado, y cómo va. La cifra grande a la derecha es
 * el promedio y debajo va el estado en una palabra, que es lo que se mira primero al buscar
 * una materia concreta entre varias.
 *
 * La barra es [LinearWavyProgressIndicator] y lleva el color de la materia, nunca uno de
 * rendimiento: su longitud mide avance, y pintarla de rojo por ir mal la haría decir dos cosas
 * a la vez. El rendimiento lo llevan la cifra y el rótulo.
 */
@Composable
fun SubjectRow(
    subject: Subject,
    calculation: SubjectGradeCalculation,
    gradingScale: GradingScale,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    /** El bloque de clase, si lo tiene: de ahí salen el aula y el profesor. */
    classSession: ClassSession? = null,
    /** Marcada dentro de una selección. Solo tiene sentido con [onLongClick] puesto. */
    selected: Boolean = false,
    /**
     * Mantener pulsado. Es lo que abre la selección múltiple: no hay botón de «seleccionar»
     * porque un botón permanente ocupa sitio en una pantalla que casi siempre se usa para
     * mirar, no para borrar.
     */
    onLongClick: (() -> Unit)? = null,
    /**
     * El asa de arrastre, si la lista se puede ordenar.
     *
     * Se dibuja siempre y no solo con algo marcado: es lo único que dice que las materias se
     * pueden mover de sitio. Un gesto que no deja rastro en la pantalla no lo encuentra nadie,
     * y esa fue la primera versión de esto: mantener pulsada la fila servía para marcar y para
     * mover a la vez, y cuál de las dos obtenías dependía de si tu dedo se movía.
     */
    dragHandle: (@Composable () -> Unit)? = null
) {
    val accent = subjectAccent(subject)
    val sections = LocalSectionColors.current
    val alert = calculation.alertLevel
    val statusColor = when (alert) {
        GradeAlertLevel.CRITICAL -> MaterialTheme.colorScheme.error
        GradeAlertLevel.NONE -> sections.onTrack
        else -> sections.onAtRiskContainer
    }
    val statusContainer = when (alert) {
        GradeAlertLevel.CRITICAL -> MaterialTheme.colorScheme.errorContainer
        GradeAlertLevel.NONE -> MaterialTheme.colorScheme.surfaceContainerHigh
        else -> sections.atRiskContainer
    }
    Surface(
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick,
                onLongClickLabel = if (onLongClick != null) stringResource(R.string.subject_row_mark) else null),
        shape = MaterialTheme.shapes.large,
        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (selected) {
                    Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.primary) {
                        Box(Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Check, stringResource(R.string.subject_row_marked), tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                } else SubjectMark(subject.name.take(1).uppercase(), accent, subject.id, 40.dp)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(subject.name, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    val context = listOfNotNull(classSession?.place?.room?.takeIf(String::isNotBlank),
                        classSession?.place?.professor?.takeIf(String::isNotBlank)).joinToString(" · ")
                    if (context.isNotBlank()) Text(context, style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
                Text(calculation.currentAverage?.let { GradingScaleUtils.formatGrade(it, gradingScale) } ?: "—",
                    style = MaterialTheme.typography.headlineSmall)
                dragHandle?.invoke()
            }
            Text(supportLine(subject, calculation, gradingScale), style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            EvaluationBar(calculation.evaluatedSemesterFraction, modifier = Modifier.fillMaxWidth(),
                color = accent, trackColor = MaterialTheme.colorScheme.outlineVariant)
            Surface(shape = MaterialTheme.shapes.small, color = statusContainer) {
                Text(outlookLabel(calculation.outlook, alert, LocalAccessibilityPreferences.current.shapesBesidesColor),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (alert == GradeAlertLevel.CRITICAL) MaterialTheme.colorScheme.onErrorContainer else statusColor)
            }
        }
    }
}


/**
 * La marca de la materia: su inicial dentro de una forma de nueve lóbulos.
 *
 * Se dibuja con [Canvas] y no recortando una caja con la forma. Recortando no funcionaba —lo
 * que salía era el rectángulo sin recortar— y el mismo fallo se coló ya una vez en el hero de
 * Inicio; dibujar el trazado no depende de que el recorte llegue a aplicarse.
 */
@Composable
internal fun SubjectMark(letter: String, color: Color, seed: String, markSize: Dp = 48.dp) {
    /*
     * La forma sale del ajuste, y solo cae en el sorteo por identificador cuando el ajuste
     * dice «Aleatorio».
     *
     * El reparto por identificador estuvo fijo desde el principio y era lo unico que habia:
     * quien queria todas las materias con la misma forma no tenia como pedirlo. Ahora es una
     * de las seis opciones —y sigue siendo la interesante, porque es la que deja distinguir
     * dos materias del mismo color— pero ya no es la unica.
     */
    // Cada materia con la suya, fijo desde el 20 sep 2026.
    val estilo = BadgeShape.ALEATORIO
    val polygon = remember(seed, estilo) { formaDeMateria(estilo, seed) }
    val path = polygon.toPath()
    val distinguishShapes = LocalAccessibilityPreferences.current.shapesBesidesColor

    Box(modifier = Modifier.size(markSize), contentAlignment = Alignment.Center) {
        if (!distinguishShapes) {
            Box(Modifier.size(markSize).clip(MaterialTheme.shapes.medium).background(color))
        } else Canvas(modifier = Modifier.size(markSize)) {
            withTransform({ scale(size.width, size.height, pivot = Offset.Zero) }) {
                drawPath(path, color)
            }
        }
        Text(
            text = letter,
            style = MaterialTheme.typography.titleMediumEmphasized,
            color = com.leoaristocrat.semesta.core.design.theme.contentColorOn(color)
        )
    }
}

/**
 * La familia de formas de Material 3 Expressive, construidas con su mismo motor de polígonos.
 *
 * Cada una cambia en tres cosas: cuántos lóbulos tiene, cuánto se hunde entre ellos y cuánto
 * se redondean las puntas. De ahí salen la galleta, el trébol, el estallido, el sol y la flor.
 */
private val MarkShapes: List<() -> RoundedPolygon> = listOf(
    { markPolygon(vertices = 9, innerRatio = 0.84f, rounding = 0.22f) },   // galleta
    { markPolygon(vertices = 4, innerRatio = 0.62f, rounding = 0.35f) },   // trébol
    { markPolygon(vertices = 12, innerRatio = 0.80f, rounding = 0.12f) },  // estallido suave
    { markPolygon(vertices = 8, innerRatio = 0.88f, rounding = 0.28f) },   // sol
    { markPolygon(vertices = 6, innerRatio = 0.66f, rounding = 0.32f) },   // flor
    { markPolygon(vertices = 7, innerRatio = 0.78f, rounding = 0.20f) },   // galleta de siete
    { markPolygon(vertices = 5, innerRatio = 0.72f, rounding = 0.30f) },   // pentágono blando
    { markPolygon(vertices = 10, innerRatio = 0.90f, rounding = 0.18f) }   // margarita
)

private fun markPolygon(vertices: Int, innerRatio: Float, rounding: Float): RoundedPolygon =
    RoundedPolygon.star(
        numVerticesPerRadius = vertices,
        radius = 0.5f,
        innerRadius = 0.5f * innerRatio,
        rounding = CornerRounding(rounding),
        centerX = 0.5f,
        centerY = 0.5f
    )

/**
 * Qué forma le toca a una materia.
 *
 * Sale de su identificador y no de un sorteo: así es distinta de la de al lado pero **siempre
 * la misma** para la misma materia. Una forma que cambiara en cada recomposición dejaría de
 * servir para reconocerla de un vistazo, que es justo para lo que está.
 */
internal fun formaDeMateria(estilo: BadgeShape, seed: String): RoundedPolygon = when (estilo) {
    /*
     * **Aqui estaba el cierre de golpe de Academico.**
     *
     * «Circulo» y «Rombo» se pedian como estrellas con el radio interior **igual** al exterior,
     * y `RoundedPolygon.star` no acepta eso: una estrella cuyas puntas y valles estan a la
     * misma distancia no es una estrella, es un poligono, y hay que pedirlo con `RoundedPolygon`
     * a secas. La excepcion saltaba al componer la primera fila de materia, asi que se llevaba
     * la pantalla antes de pintar nada — de ahi que no hubiera ni registro ni dialogo.
     */
    BadgeShape.CIRCULO -> RoundedPolygon.circle(numVertices = 12, radius = 0.5f, centerX = 0.5f, centerY = 0.5f)
    BadgeShape.GALLETA -> MarkShapes[0]()
    BadgeShape.TREBOL -> MarkShapes[1]()
    BadgeShape.SOL -> MarkShapes[3]()
    BadgeShape.ROMBO -> RoundedPolygon(
        numVertices = 4,
        radius = 0.5f,
        centerX = 0.5f,
        centerY = 0.5f,
        rounding = CornerRounding(0.06f)
    )
    // El reparto de siempre: sale del identificador y no de un sorteo, asi que es distinta de
    // la de al lado pero **siempre la misma** para la misma materia. Una forma que cambiara en
    // cada recomposicion dejaria de servir para reconocerla de un vistazo.
    BadgeShape.ALEATORIO -> MarkShapes[Math.floorMod(seed.hashCode(), MarkShapes.size)]()
}

/**
 * La línea de apoyo.
 *
 * Con la meta en riesgo cambia de tema a propósito: cuánto queda por evaluar deja de ser lo
 * útil, y lo que hace falta saber es qué nota hay que sacar en lo que falta para alcanzarla.
 */
@Composable
private fun supportLine(
    subject: Subject,
    calculation: SubjectGradeCalculation,
    gradingScale: GradingScale
): String {
    // El corte va delante cuando el usuario ha elegido uno. Mientras no lo haya elegido no se
    // nombra ninguno: la app no sabe en qué punto del semestre va, y suponerlo fue justo el
    // fallo que se corrigió al dejar activeCutId vacío de nacimiento.
    val cut = subject.chosenCutId
        ?.let { id -> subject.cutScheme.cuts.firstOrNull { it.id == id } }
        ?.name
    val state = progressState(calculation, gradingScale)
    // Mayúscula al principio y en ningún otro sitio. Las piezas se escriben en minúscula
    // porque cualquiera de ellas puede ir en medio: con el corte delante, «Falta el 35 %»
    // quedaba como «Corte 2 · Falta el 35 %», con una mayúscula suelta a media frase.
    return listOfNotNull(cut, state).joinToString(" · ").replaceFirstChar(Char::uppercase)
}

@Composable
private fun progressState(
    calculation: SubjectGradeCalculation,
    gradingScale: GradingScale
): String {
    val remaining = ((1.0 - calculation.evaluatedSemesterFraction) * 100).toInt().coerceIn(0, 100)
    val needed = calculation.neededForTarget
    return when {
        calculation.outlook == TargetOutlook.UNREACHABLE -> stringResource(R.string.subject_goal_unreachable)
        calculation.outlook == TargetOutlook.AT_RISK && needed != null ->
            stringResource(R.string.subject_needed_in_remaining, GradingScaleUtils.formatGrade(needed, gradingScale))
        calculation.outlook == TargetOutlook.NO_DATA -> stringResource(R.string.subject_no_grades_yet)
        remaining == 0 -> stringResource(R.string.subject_all_evaluated)
        else -> stringResource(R.string.subject_remaining_percentage, remaining)
    }
}

/**
 * El estado en una palabra.
 *
 * La palabra sale del pronóstico **y** de la gravedad: «EN RIESGO» y «PERDIDA» hablan del
 * aprobado, así que se reservan para cuando el aprobado está en juego de verdad. Cuando lo
 * único que falta es la meta que se puso el usuario, lo dice sin dramatismo —«BAJO META»,
 * «FUERA DE ALCANCE»— porque eso es exactamente lo que pasa.
 */
@Composable
private fun outlookLabel(
    outlook: TargetOutlook,
    alerta: GradeAlertLevel,
    showShapes: Boolean = false
): String {
    val prefix = if (showShapes) {
        when (alerta) {
            GradeAlertLevel.NONE -> if (outlook == TargetOutlook.NO_DATA) "" else "● "
            GradeAlertLevel.BEHIND -> "▲ "
            GradeAlertLevel.CRITICAL -> "■ "
        }
    } else ""
    val critico = alerta == GradeAlertLevel.CRITICAL
    val label = when (outlook) {
        TargetOutlook.NO_DATA -> stringResource(R.string.outlook_no_data)
        TargetOutlook.SECURED -> stringResource(R.string.outlook_secured)
        TargetOutlook.ON_TRACK -> stringResource(R.string.outlook_on_track)
        TargetOutlook.AT_RISK ->
            if (critico) stringResource(R.string.outlook_at_risk) else stringResource(R.string.outlook_behind)
        TargetOutlook.UNREACHABLE ->
            if (critico) stringResource(R.string.outlook_failing) else stringResource(R.string.outlook_unreachable)
    }
    return "$prefix$label"
}
