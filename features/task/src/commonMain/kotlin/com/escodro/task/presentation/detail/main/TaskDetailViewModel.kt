package com.escodro.task.presentation.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.SetAsLongTerm
import com.escodro.domain.usecase.task.UnsetAsLongTerm
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskPriority
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskPriorityMapper
import com.escodro.task.model.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Suppress("LongParameterList")
internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val updateTaskCategory: UpdateTaskCategory,
    private val updateTaskPriority: UpdateTaskPriority,
    private val setAsLongTerm: SetAsLongTerm,
    private val unsetAsLongTerm: UnsetAsLongTerm,
    private val coroutineDebouncer: CoroutineDebouncer,
    private val applicationScope: AppCoroutineScope,
    private val taskMapper: TaskMapper,
    private val taskPriorityMapper: TaskPriorityMapper,
) : ViewModel() {

    fun loadTaskInfo(taskId: TaskId): Flow<TaskDetailState> = flow {
        val task = loadTaskUseCase(taskId = taskId.value)

        if (task != null) {
            val viewTask = taskMapper.toView(task)
            emit(TaskDetailState.Loaded(viewTask))
        } else {
            emit(TaskDetailState.Error)
        }
    }

    fun updateTitle(taskId: TaskId, title: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskTitle(taskId.value, title)
        }
    }

    fun updateDescription(taskId: TaskId, description: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskDescription(taskId.value, description)
        }
    }

    fun updateCategory(taskId: TaskId, categoryId: CategoryId) =
        applicationScope.launch {
            updateTaskCategory(taskId = taskId.value, categoryId = categoryId.value)
        }

    fun updatePriority(taskId: TaskId, priority: TaskPriority) =
        applicationScope.launch {
            updateTaskPriority(taskId = taskId.value, priority = taskPriorityMapper.toDomain(priority))
        }

    fun toggleLongTerm(taskId: TaskId, isLongTerm: Boolean) =
        applicationScope.launch {
            if (isLongTerm) {
                setAsLongTerm(taskId.value)
            } else {
                unsetAsLongTerm()
            }
        }
}
