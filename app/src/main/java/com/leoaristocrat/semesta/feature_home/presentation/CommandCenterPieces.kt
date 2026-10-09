package com.leoaristocrat.semesta.feature_home.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leoaristocrat.semesta.R
import com.leoaristocrat.semesta.feature_home.domain.HomeSummary
import com.leoaristocrat.semesta.feature_user.domain.AppModule

@Composable
internal fun HomeQuickActions(summary: HomeSummary, onTask: () -> Unit, onNote: () -> Unit, onExpense: () -> Unit, onCourse: () -> Unit, onCustomize: () -> Unit) {
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        if (AppModule.TASKS in summary.enabledModules) OutlinedButton(onTask, shape = MaterialTheme.shapes.medium) { Text(stringResource(R.string.fab_task)) }
        OutlinedButton(onNote, shape = MaterialTheme.shapes.medium) { Text(stringResource(R.string.shortcut_note_short)) }
        if (AppModule.GRADES in summary.enabledModules) OutlinedButton(onCourse, shape = MaterialTheme.shapes.medium) { Text(stringResource(R.string.schedule_identity_subject)) }
        OutlinedButton(onCustomize, shape = MaterialTheme.shapes.medium) { Text(stringResource(R.string.semesta_personalize)) }
        if (AppModule.EXPENSES in summary.enabledModules) OutlinedButton(onExpense, shape = MaterialTheme.shapes.medium) { Text(stringResource(R.string.fab_expense)) }
    }
}

/** Keep each section's heading with its body when the grid flows into a second column. */
internal fun groupHomeBlocks(blocks: List<BloqueDeInicio>): List<List<BloqueDeInicio>> {
    val grouped = mutableListOf<List<BloqueDeInicio>>()
    var index = 0
    while (index < blocks.size) {
        val first = blocks[index]
        if (first.clave.endsWith("-cabecera") && index + 1 < blocks.size) {
            grouped += listOf(first, blocks[index + 1])
            index += 2
        } else {
            grouped += listOf(first)
            index++
        }
    }
    return grouped
}
