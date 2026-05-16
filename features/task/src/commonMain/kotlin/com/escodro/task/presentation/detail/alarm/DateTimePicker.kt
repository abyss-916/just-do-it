package com.escodro.task.presentation.detail.alarm

import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escodro.resources.Res
import com.escodro.resources.dialog_picker_confirm
import com.escodro.resources.dialog_picker_next
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Composable to show a date and time picker.
 *
 * @param initialDateTime a pre-existing value set by user
 * @param isDialogOpen if the dialog should be open
 * @param onCloseDialog callback called when the dialog is closed
 * @param onDateChange callback called when the date is changed
 */
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DateTimerPicker(
    initialDateTime: LocalDateTime?,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {
    if (!isDialogOpen) return

    key(initialDateTime) {
        DateTimerPickerContent(
            initialDateTime = initialDateTime,
            onCloseDialog = onCloseDialog,
            onDateChange = onDateChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun DateTimerPickerContent(
    initialDateTime: LocalDateTime?,
    onCloseDialog: () -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {
    val displayTime: LocalDateTime = remember {
        initialDateTime ?: Clock.System.now()
            .plus(duration = 1.days)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .let { tomorrow ->
                LocalDateTime(
                    year = tomorrow.year,
                    month = tomorrow.month,
                    day = tomorrow.day,
                    hour = 23,
                    minute = 59,
                )
            }
    }

    val initialSelectedDate = remember {
        initialDateTime?.toInstant(TimeZone.currentSystemDefault()) ?: Clock.System.now()
    }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDate.toEpochMilliseconds(),
    )
    var dialogState by remember { mutableStateOf<DateTimePickerState>(DateTimePickerState.DATE) }

    LaunchedEffect(dialogState) {
        if (dialogState is DateTimePickerState.DONE) {
            val doneState = dialogState as DateTimePickerState.DONE
            val date = Instant
                .fromEpochMilliseconds(datePickerState.selectedDateMillis ?: 0)
                .toLocalDateTime(TimeZone.currentSystemDefault())

            val localDateTime = LocalDateTime(
                year = date.year,
                month = date.month,
                day = date.day,
                hour = doneState.hour,
                minute = doneState.minute,
            )
            onDateChange(localDateTime)
            onCloseDialog()
        }
    }

    if (dialogState == DateTimePickerState.DATE) {
        DatePickerDialog(
            onDismissRequest = onCloseDialog,
            confirmButton = {
                Button(onClick = {
                    dialogState = DateTimePickerState.TIME
                }) {
                    Text(text = stringResource(Res.string.dialog_picker_next))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (dialogState == DateTimePickerState.TIME) {
        PlatformTimePickerDialog(
            initialDateTime = initialDateTime,
            onDismiss = {
                dialogState = DateTimePickerState.DATE
            },
            onConfirm = { hour, minute ->
                dialogState = DateTimePickerState.DONE(hour = hour, minute = minute)
            },
        )
    }
}

/**
 * Sealed class to represent the state of the [DateTimerPicker].
 */
private sealed class DateTimePickerState {

    /**
     * Date picker dialog should be shown.
     */
    object DATE : DateTimePickerState()

    /**
     * Time picker dialog should be shown.
     */
    object TIME : DateTimePickerState()

    /**
     * Selected date and time should be returned.
     */
    data class DONE(val hour: Int, val minute: Int) : DateTimePickerState()
}
