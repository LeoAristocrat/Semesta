package com.leoaristocrat.semesta.feature_support.domain

import com.leoaristocrat.semesta.core.utils.Textos
import com.leoaristocrat.semesta.R

/**
 * Por qué alguien escribe.
 *
 * Eran dos —fallo e idea— y cada uno tenía su propia puerta en Ayuda. Se juntaron en un solo
 * formulario con este selector arriba: quien escribe no siempre sabe de antemano si lo suyo es
 * un fallo o una petición, y obligarle a elegir antes de contar nada dejaba fuera todo lo que
 * no era ninguna de las dos.
 */
enum class TicketKind(private val labelRes: Int, val emoji: String) {
    BUG(R.string.ticket_kind_bug, "🐞"),
    IDEA(R.string.ticket_kind_idea, "💡"),
    OTHER(R.string.ticket_kind_other, "💬");

    val label: String get() = Textos.get(labelRes)
}

/**
 * Lo que la app sabe de sí misma y del teléfono.
 *
 * Va al final de cada ticket porque es lo primero que hay que preguntar si no está: sin versión
 * ni modelo, la mitad de los reportes acaban en «¿qué versión tienes?» y ahí se pierde el hilo.
 *
 * El número de SDK acompaña a la versión de Android porque no siempre coinciden con lo que uno
 * espera —las capas de fabricante cambian la etiqueta— y es el número con el que se comprueba
 * si algo aplica a esa versión.
 */
data class TicketContext(
    val appVersion: String,
    val androidVersion: String,
    val androidSdk: Int,
    val device: String
)

/** First-party support is email; the user reviews and sends in their own mail app. */
object SupportChannel {
    const val EMAIL = "leoaristocratjr@gmail.com"
    const val HANDLE = EMAIL
    fun topicFor(kind: TicketKind): Int? = null
    fun webUrlFor(kind: TicketKind): String = appUriFor(kind)
    fun appUriFor(kind: TicketKind): String = "mailto:$EMAIL?subject=" +
        java.net.URLEncoder.encode("Semesta · ${kind.name}", "UTF-8").replace("+", "%20")
}

/** Report text is reviewed in the mail app before sending. Contact details are optional. */
fun buildTicket(
    kind: TicketKind,
    text: String,
    contact: String?,
    context: TicketContext
): String = buildString {
    append(kind.emoji)
    append(' ')
    appendLine(kind.label)
    appendLine()
    appendLine(text.trim())
    appendLine()
    appendLine("---")
    appendLine("Semesta ${context.appVersion}")
    appendLine("Android ${context.androidVersion} (SDK ${context.androidSdk}) · ${context.device}")
    contact?.trim()?.takeIf { it.isNotEmpty() }?.let { append(Textos.get(R.string.ticket_contact, it)) }
}
