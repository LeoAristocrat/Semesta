package com.leoaristocrat.semesta.feature_setup.presentation

import java.text.Normalizer
import java.util.Locale

/** Free-text institution search; stored names are never normalised. */
object InstitutionCatalog {

    private val universities = listOf<String>()
    // Institutions are entered freely: colleges, schools, boards and training centres.
    // No incomplete directory is presented as an authoritative list of Indian institutions.


    /**
     * Sugerencias para [query], ignorando mayúsculas y tildes para que «javeriana» y
     * «Javeriana» encuentren lo mismo. Con la consulta vacía no sugiere nada: el campo es
     * opcional y no conviene empujar a rellenarlo.
     */
    fun suggestionsFor(query: String, limit: Int = 5): List<String> {
        val needle = query.normalizeForSearch()
        if (needle.length < 2) return emptyList()
        return universities
            .filter { it.normalizeForSearch().contains(needle) }
            .take(limit)
    }

    private fun String.normalizeForSearch(): String =
        Normalizer.normalize(trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
}
