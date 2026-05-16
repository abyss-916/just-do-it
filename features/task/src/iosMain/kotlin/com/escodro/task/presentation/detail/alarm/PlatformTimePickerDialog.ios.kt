package com.escodro.task.presentation.detail.alarm

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.escodro.resources.Res
import com.escodro.resources.dialog_picker_confirm
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * iOS time picker dialog using Compose Multiplatform TimePicker.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
internal actual fun PlatformTimePickerDialog(
    initialDateTime: LocalDateTime?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
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

    val timePickerState = rememberTimePickerState(
        initialHour = displayTime.hour,
        initialMinute = displayTime.minute,
    )

    var selectedHour by remember { mutableStateOf(displayTime.hour) }
    var selectedMinute by remember { mutableStateOf(displayTime.minute) }

    LaunchedEffect(timePickerState) {
        snapshotFlow { timePickerState.hour to timePickerState.minute }
            .collect { (h, m) ->
                selectedHour = h
                selectedMinute = m
            }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedHour, selectedMinute)
            }) {
                Text(text = stringResource(Res.string.dialog_picker_confirm))
            }
        },
        text = {
            TimePicker(state = timePickerState)
        },
    )
}
