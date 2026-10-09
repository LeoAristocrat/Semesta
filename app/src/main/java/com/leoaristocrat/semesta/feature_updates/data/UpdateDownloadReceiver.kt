package com.leoaristocrat.semesta.feature_updates.data

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.leoaristocrat.semesta.core.di.SemestaEntryPoint
import dagger.hilt.android.EntryPointAccessors

/**
 * El sistema avisa de que una descarga terminó, aunque la app esté cerrada.
 *
 * Es lo que convierte el aviso de progreso en «lista para instalar» cuando la descarga la
 * empezó el trabajo de fondo con Wi-Fi: el sondeo de la app muere con su proceso, y sin esto
 * el aviso se quedaba congelado en el último porcentaje visto.
 */
class UpdateDownloadReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != DownloadManager.ACTION_DOWNLOAD_COMPLETE) return
        val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)
        if (id < 0) return
        val repository = EntryPointAccessors
            .fromApplication(context.applicationContext, SemestaEntryPoint::class.java)
            .updateRepository()
        (repository as? GitHubReleaseUpdateRepository)?.onDownloadCompleted(id)
    }
}
