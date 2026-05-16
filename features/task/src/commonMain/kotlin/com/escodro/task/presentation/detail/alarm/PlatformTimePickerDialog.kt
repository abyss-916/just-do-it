package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateTime

/**
 * Platform-specific time picker dialog.
 */
@Composable
internal expect fun PlatformTimePickerDialog(
    initialDateTime: LocalDateTime?,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
)
