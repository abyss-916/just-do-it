package com.escodro.task.presentation.detail.alarm

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Android native time picker dialog.
 *
 * Subclasses TimePickerDialog to fix the bug where onTimeSet fires on
 * dismiss with the initial values. We override the button click listeners
 * to track whether OK or Cancel was pressed, and only call onConfirm
 * when OK was explicitly clicked.
 */
@OptIn(ExperimentalTime::class)
@Composable
internal actual fun PlatformTimePickerDialog(
    initialDateTime: LocalDateTime?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    val context = LocalContext.current
    val onConfirmRef = rememberUpdatedState(onConfirm)

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

    DisposableEffect(displayTime) {
        val dialog = FixedTimePickerDialog(
            context = context,
            initialHour = displayTime.hour,
            initialMinute = displayTime.minute,
            is24Hour = true,
            onConfirm = onConfirmRef,
        )

        dialog.show()
        onDispose {
            dialog.dismiss()
        }
    }
}

/**
 * Custom TimePickerDialog that fixes the Android bug where onTimeSet fires
 * on dismiss with the initial values instead of user-selected values.
 *
 * The fix: override onClick to read values directly from the TimePicker view,
 * then call onConfirm only when OK was explicitly clicked. Cancel/back/dismiss
 * does NOT trigger onConfirm.
 */
private class FixedTimePickerDialog(
    context: Context,
    private val initialHour: Int,
    private val initialMinute: Int,
    is24Hour: Boolean,
    private val onConfirm: androidx.compose.runtime.State<(Int, Int) -> Unit>,
) : TimePickerDialog(
        context,
        { _, hour, minute -> onConfirm.value(hour, minute) },
        initialHour,
        initialMinute,
        is24Hour,
    ) {
    @Suppress("DEPRECATION")
    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val timePicker = findViewById<TimePicker>(
                    android.content.res.Resources.getSystem()
                        .getIdentifier("timePicker", "id", "android"),
                )
                if (timePicker != null) {
                    onConfirm.value(timePicker.currentHour, timePicker.currentMinute)
                } else {
                    onConfirm.value(initialHour, initialMinute)
                }
                dismiss()
            }

            DialogInterface.BUTTON_NEGATIVE -> {
                cancel()
            }
        }
    }

    override fun onStop() {
        // Don't call super.onStop() — it would call onTimeSet again or with wrong values
    }
}
