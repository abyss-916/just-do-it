package com.escodro.task.presentation.detail.duedate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.resources.Res
import com.escodro.resources.dialog_picker_confirm
import com.escodro.resources.task_detail_cd_icon_due_date
import com.escodro.resources.task_detail_due_date_no_date
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun DueDateSelection(
    dueDate: LocalDateTime?,
    onDueDateUpdate: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    DueDatePickerDialog(
        showDialog = showDatePicker,
        onDismiss = { showDatePicker = false },
        onDateSelected = { millis ->
            val newDate = millis?.let { epochToLocalDateTime(it) }
            onDueDateUpdate(newDate)
            showDatePicker = false
        },
    )

    TaskDetailSectionContent(
        modifier = modifier
            .height(56.dp)
            .clickable { showDatePicker = true },
        imageVector = Icons.Outlined.CalendarMonth,
        contentDescription = stringResource(Res.string.task_detail_cd_icon_due_date),
    ) {
        DueDateInfo(
            dueDate = dueDate,
            onRemoveDate = { onDueDateUpdate(null) },
        )
    }
}

@Composable
private fun DueDateInfo(
    dueDate: LocalDateTime?,
    onRemoveDate: () -> Unit,
) {
    if (dueDate == null) {
        Text(
            text = stringResource(Res.string.task_detail_due_date_no_date),
            color = MaterialTheme.colorScheme.outline,
        )
    } else {
        DueDateSet(
            dueDate = dueDate,
            onRemoveClick = onRemoveDate,
        )
    }
}

@Composable
private fun DueDateSet(dueDate: LocalDateTime, onRemoveClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = dueDate.date.toString(),
            color = MaterialTheme.colorScheme.outline,
        )
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Remove due date",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DueDatePickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit,
) {
    if (!showDialog) return

    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
            }) {
                Text(text = stringResource(Res.string.dialog_picker_confirm))
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

@Suppress("DEPRECATION")
private fun epochToLocalDateTime(epoch: Long): LocalDateTime =
    Instant.fromEpochMilliseconds(epoch).toLocalDateTime(TimeZone.currentSystemDefault())
