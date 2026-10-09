package com.leoaristocrat.semesta.core.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.leoaristocrat.semesta.feature_tasks.domain.TasksRepository
import com.leoaristocrat.semesta.feature_updates.domain.UpdateRepository
import com.leoaristocrat.semesta.feature_user.domain.UserRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SemestaEntryPoint {
    fun userRepository(): UserRepository

    /** Para el trabajo de fondo y el receptor de descargas, que no pasan por Hilt. */
    fun updateRepository(): UpdateRepository

    /** Para la barra de abajo, que cuenta las entregas pendientes sin ViewModel propio. */
    fun tasksRepository(): TasksRepository
}

@Composable
fun rememberSemestaEntryPoint(): SemestaEntryPoint {
    val context = LocalContext.current
    return remember(context) {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            SemestaEntryPoint::class.java
        )
    }
}
