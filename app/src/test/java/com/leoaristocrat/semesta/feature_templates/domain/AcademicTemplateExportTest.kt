package com.leoaristocrat.semesta.feature_templates.domain

import com.leoaristocrat.semesta.TextosDePrueba
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AcademicTemplateExportTest {

    @Before
    fun instalarTextos() {
        TextosDePrueba.instalar()
    }
    @Test
    fun exportTextIncludesTemplateChecklistAndApaSections() {
        val template = AcademicTemplateLibrary.essayTemplates.first()

        val exported = template.exportText(completedChecklistIds = listOf("topic"))

        assertTrue(exported.contains(template.title))
        assertTrue(exported.contains("Checklist"))
        assertTrue(exported.contains("[x] Defined Topic"))
        assertTrue(exported.contains("[ ] Reviewed Sources"))
        assertTrue(exported.contains("Structure"))
        assertTrue(exported.contains("APA basics"))
        assertTrue(exported.contains("APA references"))
    }

    @Test
    fun buildApaReferenceDraftFormatsStructuredSources() {
        val exported = buildApaReferenceDraft("García, M. | 2024 | Aprendizaje activo | Revista Educación")

        assertTrue(exported.contains("García, M. (2024). Aprendizaje activo. Revista Educación."))
    }
}
