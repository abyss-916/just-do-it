package com.escodro.task.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.escodro.alarmapi.AlarmPermission
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.components.textfield.AlkaaInputTextField
import com.escodro.resources.Res
import com.escodro.resources.dialog_picker_confirm
import com.escodro.resources.task_add_cancel
import com.escodro.resources.task_add_description
import com.escodro.resources.task_add_due_date
import com.escodro.resources.task_add_label
import com.escodro.resources.task_add_long_term
import com.escodro.resources.task_add_save
import com.escodro.resources.task_add_set_due_date
import com.escodro.resources.task_limit_reached
import com.escodro.resources.task_long_term_exists
import com.escodro.task.model.AlarmInterval
import com.escodro.task.model.TaskPriority
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.alarm.AlarmSelection
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.detail.priority.PrioritySelection
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddTaskBottomSheet(
    onHideBottomSheet: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onHideBottomSheet()
        },
    ) {
        AddTaskBottomSheetContent(
            onHideBottomSheet = onHideBottomSheet,
            onStateConsumed = {
                scope.launch { sheetState.hide() }
                onHideBottomSheet()
            },
        )
    }
}

@Suppress("LongMethod")
@Composable
internal fun AddTaskBottomSheetContent(
    addTaskViewModel: AddTaskViewModel = koinInject(),
    categoryViewModel: CategoryListViewModel = koinInject(),
    alarmPermission: AlarmPermission = koinInject(),
    onHideBottomSheet: () -> Unit,
    onStateConsumed: () -> Unit = onHideBottomSheet,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarMessage = when (addTaskViewModel.state) {
        is AddTaskState.TaskLimitReached -> stringResource(Res.string.task_limit_reached)
        else -> null
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
            )
            addTaskViewModel.consumeState()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        AddTaskBottomSheetInnerContent(
            addTaskViewModel = addTaskViewModel,
            categoryViewModel = categoryViewModel,
            alarmPermission = alarmPermission,
            onHideBottomSheet = onHideBottomSheet,
            onStateConsumed = onStateConsumed,
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun AddTaskBottomSheetInnerContent(
    addTaskViewModel: AddTaskViewModel,
    categoryViewModel: CategoryListViewModel,
    alarmPermission: AlarmPermission,
    onHideBottomSheet: () -> Unit,
    onStateConsumed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        var taskInputText: String by rememberSaveable { mutableStateOf("") }
        var taskDescription: String by rememberSaveable { mutableStateOf("") }
        var taskDueDate: Long? by rememberSaveable { mutableStateOf(null) }
        var showDueDatePicker: Boolean by rememberSaveable { mutableStateOf(false) }
        var taskAlarmDate: Long? by rememberSaveable { mutableStateOf(null) }
        var taskPriority: TaskPriority by rememberSaveable { mutableStateOf(TaskPriority.NONE) }
        val categoryState by remember(categoryViewModel) {
            categoryViewModel.loadCategories()
        }.collectAsState(initial = CategoryState.Empty)
        var currentCategory by rememberSaveable { mutableStateOf<CategoryId?>(null) }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            delay(FocusDelay)
            focusRequester.requestFocus()
        }

        AlkaaInputTextField(
            label = stringResource(Res.string.task_add_label),
            text = taskInputText,
            onTextChange = { text -> taskInputText = text },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )

        AlkaaInputTextField(
            label = stringResource(Res.string.task_add_description),
            text = taskDescription,
            onTextChange = { text -> taskDescription = text },
            modifier = Modifier.fillMaxWidth(),
        )

        CategorySelection(
            state = categoryState,
            currentCategory = currentCategory?.value,
            onCategoryChange = { categoryId -> currentCategory = categoryId },
            contentPadding = PaddingValues(horizontal = 8.dp),
        )

        PrioritySelection(
            currentPriority = taskPriority,
            onPriorityChange = { taskPriority = it },
            contentPadding = PaddingValues(horizontal = 8.dp),
        )

        DueDateSelectionRow(
            dueDateMillis = taskDueDate,
            onDueDateClick = { showDueDatePicker = true },
        )

        AlarmSelection(
            calendar = getLocalDateTimeFromEpoch(taskAlarmDate),
            onAlarmUpdate = { dateTime -> taskAlarmDate = getEpochFromLocalDateTime(dateTime) },
            hasExactAlarmPermission = { alarmPermission.hasExactAlarmPermission() },
            openExactAlarmPermissionScreen = { alarmPermission.openExactAlarmPermissionScreen() },
            openAppSettingsScreen = { alarmPermission.openAppSettings() },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                onClick = onHideBottomSheet,
            ) {
                Text(
                    text = stringResource(Res.string.task_add_cancel),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                onClick = {
                    addTaskViewModel.addTask(
                        title = taskInputText,
                        description = taskDescription.takeIf { it.isNotBlank() },
                        categoryId = currentCategory,
                        dueDate = getLocalDateTimeFromEpoch(taskDueDate),
                        alarmDate = getLocalDateTimeFromEpoch(taskAlarmDate),
                        alarmInterval = AlarmInterval.NEVER,
                        priority = taskPriority,
                    )
                    taskInputText = ""
                    taskDescription = ""
                    taskPriority = TaskPriority.NONE
                    taskDueDate = null
                    taskAlarmDate = null
                    onHideBottomSheet()
                },
            ) {
                Text(
                    text = stringResource(Res.string.task_add_save),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        if (showDueDatePicker) {
            DueDatePickerDialog(
                onDismiss = { showDueDatePicker = false },
                onDateSelected = { millis ->
                    taskDueDate = millis
                    showDueDatePicker = false
                },
            )
        }
    }
}

@Composable
private fun DueDateSelectionRow(
    dueDateMillis: Long?,
    onDueDateClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(onClick = onDueDateClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = stringResource(Res.string.task_add_set_due_date),
            modifier = Modifier.width(24.dp),
        )
        Text(
            text = if (dueDateMillis != null) {
                epochToLocalDate(dueDateMillis).toString()
            } else {
                stringResource(Res.string.task_add_set_due_date)
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DueDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit,
) {
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
private fun epochToLocalDate(epoch: Long): LocalDate =
    Instant.fromEpochMilliseconds(epoch).toLocalDateTime(TimeZone.currentSystemDefault()).date

private fun getLocalDateTimeFromEpoch(epoch: Long?): LocalDateTime? = epoch?.let {
    val localDate = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault())
    LocalDateTime(
        year = localDate.year,
        month = localDate.month,
        day = localDate.day,
        hour = localDate.hour,
        minute = localDate.minute,
    )
}

private fun getEpochFromLocalDateTime(dateTime: LocalDateTime?): Long? = dateTime?.let {
    dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

private const val FocusDelay = 500L
