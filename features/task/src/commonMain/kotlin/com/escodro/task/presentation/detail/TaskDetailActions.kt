package com.escodro.task.presentation.detail

import com.escodro.task.model.TaskPriority
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.datetime.LocalDateTime

/**
 * Actions to be performed by the [com.escodro.task.presentation.detail.main.TaskDetailScreen].
 */
internal data class TaskDetailActions(
    val onTitleChange: (String) -> Unit = {},
    val onDescriptionChange: (String) -> Unit = {},
    val onCategoryChange: (CategoryId) -> Unit = {},
    val onPriorityChange: (TaskPriority) -> Unit = {},
    val onDueDateChange: (LocalDateTime?) -> Unit = {},
    val onAlarmChange: (LocalDateTime?) -> Unit = {},
    val hasExactAlarmPermission: () -> Boolean = { false },
    val openExactAlarmPermissionScreen: () -> Unit = {},
    val openAppSettingsScreen: () -> Unit = {},
    val onUpPress: () -> Unit = {},
)
