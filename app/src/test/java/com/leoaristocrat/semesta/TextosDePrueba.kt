package com.leoaristocrat.semesta

import com.leoaristocrat.semesta.core.utils.Textos
import java.io.File
import java.util.Locale

/** Resolves the shipped English XML resources for JVM tests without Android context. */
object TextosDePrueba {
    private val nombres: Map<Int, String> by lazy {
        R.string::class.java.fields.associate { it.getInt(null) to it.name }
    }
    private val porIdioma = HashMap<String, Map<String, String>>()

    private fun tabla(idioma: String): Map<String, String> = porIdioma.getOrPut(idioma) {
        val carpeta = "values-en"
        val raiz = generateSequence(File(System.getProperty("user.dir") ?: ".")) { it.parentFile }
            .map { File(it, "app/src/main/res") }
            .firstOrNull { it.isDirectory }
            ?: File(System.getProperty("user.dir") ?: ".", "src/main/res")
        val xml = listOf("values", carpeta).distinct().flatMap { File(raiz, it).listFiles()?.filter { it.extension == "xml" } ?: emptyList() }
            .joinToString("\n") { it.readText(Charsets.UTF_8) }
        Regex("""<string\s+[^>]*?name="([^"]+)"[^>]*?>(.*?)</string>""", RegexOption.DOT_MATCHES_ALL)
            .findAll(xml)
            .associate { it.groupValues[1] to desescapar(it.groupValues[2]) }
    }

    private fun desescapar(s: String): String = s
        .replace("\\'", "'")
        .replace("\\\"", "\"")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("\\n", "\n")
        .trim('"')

    private val nombresDeListas: Map<Int, String> by lazy {
        R.array::class.java.fields.associate { it.getInt(null) to it.name }
    }

    private fun listas(idioma: String): Map<String, List<String>> {
        val carpeta = "values-en"
        val raiz = generateSequence(File(System.getProperty("user.dir") ?: ".")) { it.parentFile }
            .map { File(it, "app/src/main/res") }
            .firstOrNull { it.isDirectory }
            ?: File(System.getProperty("user.dir") ?: ".", "src/main/res")
        val xml = listOf("values", carpeta).distinct().flatMap { File(raiz, it).listFiles()?.filter { it.extension == "xml" } ?: emptyList() }
            .joinToString("\n") { it.readText(Charsets.UTF_8) }
        return Regex("""<string-array name="([^"]+)">(.*?)</string-array>""", RegexOption.DOT_MATCHES_ALL)
            .findAll(xml)
            .associate { m ->
                m.groupValues[1] to Regex("<item>(.*?)</item>").findAll(m.groupValues[2]).map { desescapar(it.groupValues[1]) }.toList()
            }
    }

    fun instalar() {
        Textos.proveedorDeListas = { id ->
            val nombre = nombresDeListas[id] ?: error("no hay lista con id $id")
            listas("en")[nombre] ?: error("falta la lista «$nombre»")
        }
        Textos.proveedor = { id, args ->
            val nombre = nombres[id] ?: error("no hay recurso con id $id")
            val idioma = "en"
            val plantilla = tabla(idioma)[nombre]
                ?: tabla("en")[nombre]
                ?: error("falta la cadena «$nombre» en strings.xml")
            if (args.isEmpty()) plantilla else String.format(Locale.ENGLISH, plantilla, *args)
        }
    }
}
