package com.escodro.task.presentation.longterm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.textfield.AlkaaInputTextField
import com.escodro.resources.Res
import com.escodro.resources.long_term_task_add
import com.escodro.resources.long_term_task_back
import com.escodro.resources.long_term_task_delete
import com.escodro.resources.long_term_task_delete_confirm
import com.escodro.resources.long_term_task_edit
import com.escodro.resources.long_term_task_exists
import com.escodro.resources.long_term_task_title
import com.escodro.resources.task_add_description
import com.escodro.resources.task_add_label
import com.escodro.resources.task_add_save
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

sealed class LongTermTaskActionState {
    data object None : LongTermTaskActionState()

    data object TaskAdded : LongTermTaskActionState()

    data object TaskUpdated : LongTermTaskActionState()

    data object TaskDeleted : LongTermTaskActionState()

    data object TaskExists : LongTermTaskActionState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongParameterList")
@Composable
fun LongTermTaskBottomSheet(
    onDismiss: () -> Unit,
    onConsumed: () -> Unit = onDismiss,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onDismiss()
        },
    ) {
        LongTermTaskBottomSheetContent(
            onDismiss = onDismiss,
            onConsumed = {
                scope.launch { sheetState.hide() }
                onConsumed()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
@Composable
internal fun LongTermTaskBottomSheetContent(
    viewModel: LongTermTaskViewModel = koinInject(),
    onDismiss: () -> Unit,
    onConsumed: () -> Unit = onDismiss,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var actionState by remember { mutableStateOf<LongTermTaskActionState>(LongTermTaskActionState.None) }

    val snackbarMessage = when (actionState) {
        is LongTermTaskActionState.TaskAdded -> stringResource(Res.string.long_term_task_edit)
        is LongTermTaskActionState.TaskUpdated -> stringResource(Res.string.long_term_task_edit)
        is LongTermTaskActionState.TaskDeleted -> stringResource(Res.string.long_term_task_delete_confirm)
        is LongTermTaskActionState.TaskExists -> stringResource(Res.string.long_term_task_exists)
        else -> null
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short,
            )
            actionState = LongTermTaskActionState.None
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        LongTermTaskBottomSheetInnerContent(
            viewModel = viewModel,
            onDismiss = onDismiss,
            onConsumed = onConsumed,
            onActionStateChange = { actionState = it },
            modifier = Modifier.padding(paddingValues),
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun LongTermTaskBottomSheetInnerContent(
    viewModel: LongTermTaskViewModel,
    onDismiss: () -> Unit,
    onConsumed: () -> Unit,
    onActionStateChange: (LongTermTaskActionState) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val taskState by remember(viewModel, LongTermTaskViewModel.refreshKey) {
        viewModel.loadLongTermTask()
    }.collectAsState(initial = LongTermTaskState.Loading)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxWidth(0.5f)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when (taskState) {
            is LongTermTaskState.Loading -> {
                Text(
                    text = "Loading...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )
            }

            is LongTermTaskState.Empty -> {
                AddLongTermTaskForm(
                    onSave = { title, description ->
                        coroutineScope.launch {
                            viewModel.addTask(
                                title = title,
                                description = description,
                            )
                            onActionStateChange(LongTermTaskActionState.TaskAdded)
                            onConsumed()
                        }
                    },
                    onCancel = onDismiss,
                )
            }

            is LongTermTaskState.Loaded -> {
                val task = (taskState as LongTermTaskState.Loaded).task
                EditLongTermTaskForm(
                    task = task,
                    onDelete = {
                        coroutineScope.launch {
                            viewModel.deleteTask()
                            onActionStateChange(LongTermTaskActionState.TaskDeleted)
                            onConsumed()
                        }
                    },
                    onCancel = onDismiss,
                )
            }

            LongTermTaskState.Error -> {
                Text(
                    text = "Error loading task",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun AddLongTermTaskForm(
    onSave: (String, String?) -> Unit,
    onCancel: () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Text(
        text = stringResource(Res.string.long_term_task_title),
        style = MaterialTheme.typography.titleMedium,
    )

    AlkaaInputTextField(
        label = stringResource(Res.string.task_add_label),
        text = title,
        onTextChange = { title = it },
        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
    )

    AlkaaInputTextField(
        label = stringResource(Res.string.task_add_description),
        text = description,
        onTextChange = { description = it },
        modifier = Modifier.fillMaxWidth(),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onCancel,
        ) {
            Text(text = stringResource(Res.string.long_term_task_back))
        }
        Button(
            modifier = Modifier.weight(1f).height(48.dp),
            onClick = { onSave(title, description.takeIf { it.isNotBlank() }) },
        ) {
            Text(text = stringResource(Res.string.task_add_save))
        }
    }
}

@Composable
private fun EditLongTermTaskForm(
    task: com.escodro.task.model.Task,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
) {
    var title by rememberSaveable { mutableStateOf(task.title) }
    var description by rememberSaveable { mutableStateOf(task.description ?: "") }

    Text(
        text = stringResource(Res.string.long_term_task_delete),
        style = MaterialTheme.typography.titleMedium,
    )

    OutlinedTextField(
        value = title,
        onValueChange = {},
        enabled = false,
        label = {
            Text(
                text = stringResource(Res.string.task_add_label),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )

    OutlinedTextField(
        value = description,
        onValueChange = {},
        enabled = false,
        label = {
            Text(
                text = stringResource(Res.string.task_add_description),
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier.fillMaxWidth(),
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onCancel,
        ) {
            Text(text = stringResource(Res.string.long_term_task_back))
        }
        Button(
            modifier = Modifier.weight(1f).height(48.dp),
            onClick = { onDelete() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        ) {
            Text(text = stringResource(Res.string.long_term_task_delete))
        }
    }
}
