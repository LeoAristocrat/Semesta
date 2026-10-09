package com.leoaristocrat.semesta.feature_support.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SupportTicketTest {

    private val contexto = TicketContext(
        appVersion = "1.1.0-alpha.1",
        androidVersion = "14",
        androidSdk = 34,
        device = "Pixel 8"
    )

    @Test
    fun `el ticket lleva el motivo, el texto y de donde sale`() {
        val ticket = buildTicket(
            TicketKind.BUG,
            "El horario no muestra la clase del sábado.",
            contact = null,
            context = contexto
        )

        assertTrue(ticket.startsWith("🐞 Bug"))
        assertTrue(ticket.contains("El horario no muestra la clase del sábado."))
        assertTrue(ticket.contains("Semesta 1.1.0-alpha.1"))
        assertTrue(ticket.contains("Android 14 (SDK 34) · Pixel 8"))
    }

    @Test
    fun `los espacios de mas no viajan`() {
        val ticket = buildTicket(
            TicketKind.IDEA,
            "   Poder exportar el horario.\n\n  ",
            contact = null,
            context = contexto
        )

        assertTrue(ticket.contains("Poder exportar el horario."))
        assertTrue("no se cuelan líneas en blanco al final", !ticket.contains("\n\n\n"))
    }

    @Test
    fun `el contacto solo aparece si se escribio`() {
        // Es opcional de verdad: sin él, el ticket no debe llevar una línea vacía esperando
        // a que alguien la rellene a mano.
        val sin = buildTicket(TicketKind.BUG, "algo", contact = null, context = contexto)
        val vacio = buildTicket(TicketKind.BUG, "algo", contact = "   ", context = contexto)
        val con = buildTicket(TicketKind.BUG, "algo", contact = " student@example.com ", context = contexto)

        assertFalse(sin.contains("Contact"))
        assertFalse(vacio.contains("Contact"))
        assertTrue(con.contains("Contact: student@example.com"))
    }

    @Test
    fun `support opens canonical email with distinct encoded subjects`() {
        val bug = SupportChannel.appUriFor(TicketKind.BUG)
        val idea = SupportChannel.appUriFor(TicketKind.IDEA)
        assertTrue(bug.startsWith("mailto:leoaristocratjr@gmail.com?subject=Semesta"))
        assertTrue(bug.endsWith("BUG"))
        assertTrue(idea.endsWith("IDEA"))
        assertFalse(bug.contains(" "))
        assertEquals(bug, SupportChannel.webUrlFor(TicketKind.BUG))
        assertEquals(null, SupportChannel.topicFor(TicketKind.OTHER))
    }

    @Test
    fun `una sugerencia se distingue de un fallo a primera vista`() {
        val fallo = buildTicket(TicketKind.BUG, "algo", contact = null, context = contexto)
        val idea = buildTicket(TicketKind.IDEA, "algo", contact = null, context = contexto)
        val otro = buildTicket(TicketKind.OTHER, "algo", contact = null, context = contexto)

        assertTrue(fallo.lineSequence().first().contains("Bug"))
        assertTrue(idea.lineSequence().first().contains("Suggestion"))
        assertTrue(otro.lineSequence().first().contains("Other"))
    }
}
