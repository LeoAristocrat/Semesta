package com.leoaristocrat.semesta.core.utils

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import java.util.Locale
import androidx.annotation.ArrayRes

/** Resolves English resources for ViewModels, domain code and background notifications. */
object Textos {
    /** Resource resolver, initialized before dependency injection. */
    @Volatile
    var proveedor: ((id: Int, args: Array<out Any>) -> String)? = null

    /** String-array resolver. */
    @Volatile
    var proveedorDeListas: ((id: Int) -> List<String>)? = null

    fun get(@StringRes id: Int, vararg args: Any): String {
        val p = proveedor ?: error("Text resource provider is not initialized by SemestaApplication")
        return p(id, args)
    }

    fun lista(@ArrayRes id: Int): List<String> {
        val p = proveedorDeListas ?: error("String-array resource provider is not initialized")
        return p(id)
    }

    /** Initializes resolvers with the application context. */
    fun desde(context: Context) {
        val app = context.applicationContext
        var cache: Pair<Locale, Context>? = null
        fun contexto(): Context {
            val idioma = Locale.ENGLISH
            return cache?.takeIf { it.first == idioma }?.second ?: run {
                val config = Configuration(app.resources.configuration).apply { setLocale(idioma) }
                app.createConfigurationContext(config).also { cache = idioma to it }
            }
        }
        proveedor = { id, args ->
            if (args.isEmpty()) contexto().getString(id)
            else contexto().getString(id, *args)
        }
        proveedorDeListas = { id -> contexto().resources.getStringArray(id).toList() }
    }
}
