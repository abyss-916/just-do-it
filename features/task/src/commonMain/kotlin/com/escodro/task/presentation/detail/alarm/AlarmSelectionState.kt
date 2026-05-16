package com.escodro.task.presentation.detail.alarm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escodro.permission.api.PermissionController
import kotlinx.datetime.LocalDateTime

/**
 * State holder for the [AlarmSelection] composable.
 */
class AlarmSelectionState(
    calendar: LocalDateTime?,
    permissionsController: PermissionController,
) {

    /**
     * The [PermissionController] to request the permissions on each platform.
     */
    var permissionsController by mutableStateOf(permissionsController)

    /**
     * The alarm date, if set.
     */
    var date by mutableStateOf(calendar)

    /**
     * The Exact Alarm permission dialog visibility state.
     */
    var isExactAlarmDialogOpen by mutableStateOf(false)

    /**
     * The Notification permission dialog visibility state.
     */
    var isNotificationDialogOpen by mutableStateOf(false)

    /**
     * The Notification Rationale dialog visibility state.
     */
    var isRationaleDialogOpen by mutableStateOf(false)

    /**
     * The Date and Time Picker dialog visibility state.
     */
    var isDateTimePickerDialogOpen by mutableStateOf(false)
}

@Composable
internal fun rememberAlarmSelectionState(
    calendar: LocalDateTime?,
    permissionsController: PermissionController,
): AlarmSelectionState =
    remember(calendar, permissionsController) {
        AlarmSelectionState(
            calendar = calendar,
            permissionsController = permissionsController,
        )
    }
