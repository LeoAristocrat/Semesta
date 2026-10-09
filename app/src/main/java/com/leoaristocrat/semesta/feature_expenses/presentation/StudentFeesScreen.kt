@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.leoaristocrat.semesta.feature_expenses.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoaristocrat.semesta.R
import com.leoaristocrat.semesta.core.design.components.LargeTitleScaffold
import com.leoaristocrat.semesta.core.design.components.SemestaConfirmDeleteDialog
import com.leoaristocrat.semesta.core.utils.CurrencyFormatter
import com.leoaristocrat.semesta.feature_expenses.domain.StudentFee
import com.leoaristocrat.semesta.feature_profile.presentation.ProfileViewModel
import com.leoaristocrat.semesta.feature_user.domain.CurrencyPreference
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.UUID

private val FeeDate = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT)
private fun rupees(value: Long) = CurrencyFormatter.format(value, CurrencyPreference.INR, includeCode = false)

@Composable
fun StudentFeesScreen(onBackClick: () -> Unit, viewModel: ProfileViewModel = hiltViewModel()) {
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val fees = profile?.feePlans.orEmpty().sortedWith(compareBy<StudentFee> { it.remaining == 0 }.thenBy { it.dueEpochDay })
    var editor by rememberSaveable { mutableStateOf<String?>(null) }
    var deletion by rememberSaveable { mutableStateOf<String?>(null) }
    val today = LocalDate.now()
    val reminderLabel = stringResource(R.string.india_fee_reminders)
    LargeTitleScaffold(title = stringResource(R.string.india_fees_title), onBackClick = onBackClick) {
        item {
            Text(stringResource(R.string.india_fees_desc), style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.india_fees_balance, rupees(fees.sumOf { it.remaining.toLong() })),
                style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            Button(onClick = { editor = "new" }, enabled = profile != null) { Text(stringResource(R.string.india_fees_add)) }
        }
        if (fees.isEmpty()) item {
            Text(stringResource(R.string.india_fees_empty), Modifier.padding(vertical = 24.dp),
                style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        fees.forEach { fee -> item(key = fee.id) {
            Column(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(fee.name, style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.india_fees_paid, rupees(fee.paid.toLong()), rupees(fee.amount.toLong())))
                LinearProgressIndicator(progress = { fee.paid.toFloat() / fee.amount }, modifier = Modifier.fillMaxWidth())
                Text(when {
                    fee.remaining == 0 -> stringResource(R.string.india_fees_settled)
                    else -> stringResource(R.string.india_fees_due, LocalDate.ofEpochDay(fee.dueEpochDay).format(FeeDate))
                }, style = MaterialTheme.typography.bodyMedium)
                if (fee.overdue(today)) Text(stringResource(R.string.india_fees_overdue), color = MaterialTheme.colorScheme.error)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { editor = fee.id }) { Text(stringResource(R.string.india_fees_edit)) }
                    TextButton(onClick = { deletion = fee.id }) { Text(stringResource(R.string.india_fees_delete), color = MaterialTheme.colorScheme.error) }
                }
                HorizontalDivider()
            }
        } }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(stringResource(R.string.india_fee_reminders), Modifier.weight(1f))
                Switch(checked = profile?.feeRemindersEnabled ?: true, onCheckedChange = viewModel::setFeeReminders, enabled = profile != null,
                    modifier = Modifier.semantics { contentDescription = reminderLabel })
            }
        }
        item { Text(stringResource(R.string.india_fees_note), style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
    editor?.let { id ->
        FeeEditor(fee = fees.firstOrNull { it.id == id }, onDismiss = { editor = null }, onSave = {
            viewModel.saveFee(it); editor = null
        })
    }
    deletion?.let { id -> SemestaConfirmDeleteDialog(
        title = stringResource(R.string.india_fees_delete), body = stringResource(R.string.india_fees_delete_desc),
        onConfirm = { viewModel.deleteFee(id); deletion = null }, onDismiss = { deletion = null }) }
}

@Composable
private fun FeeEditor(fee: StudentFee?, onDismiss: () -> Unit, onSave: (StudentFee) -> Unit) {
    var name by rememberSaveable(fee?.id) { mutableStateOf(fee?.name.orEmpty()) }
    var amount by rememberSaveable(fee?.id) { mutableStateOf(fee?.amount?.toString().orEmpty()) }
    var paid by rememberSaveable(fee?.id) { mutableStateOf(fee?.paid?.toString() ?: "0") }
    var date by rememberSaveable(fee?.id) { mutableStateOf((fee?.dueEpochDay?.let(LocalDate::ofEpochDay) ?: LocalDate.now()).format(FeeDate)) }
    var error by rememberSaveable { mutableStateOf(false) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(stringResource(R.string.india_fees_title)) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(name, { name = it.take(100); error = false }, label = { Text(stringResource(R.string.india_fees_name)) }, singleLine = true)
                OutlinedTextField(amount, { amount = it; error = false }, label = { Text(stringResource(R.string.india_fees_amount)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                OutlinedTextField(paid, { paid = it; error = false }, label = { Text(stringResource(R.string.india_fees_payment)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                OutlinedTextField(date, { date = it; error = false }, label = { Text(stringResource(R.string.india_fees_date)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii), singleLine = true)
                if (error) Text(stringResource(R.string.india_fees_error), color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite })
            }
        }, confirmButton = {
            val focusManager = LocalFocusManager.current
            TextButton(onClick = {
            focusManager.clearFocus(force = true)
            val due = runCatching { LocalDate.parse(date.trim(), FeeDate) }.getOrNull()
            val total = amount.trim().toIntOrNull(); val alreadyPaid = paid.trim().toIntOrNull()
            val value = if (due != null && total != null && alreadyPaid != null)
                StudentFee(fee?.id ?: UUID.randomUUID().toString(), name.trim(), total, alreadyPaid, due.toEpochDay()) else null
            if (value?.valid == true) onSave(value) else error = true
        }) { Text(stringResource(R.string.action_save)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) } })
}
