package com.escodro.task.presentation.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskDueDate
import com.escodro.domain.usecase.task.UpdateTaskPriority
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskPriorityMapper
import com.escodro.task.model.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

@Suppress("LongParameterList")
internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val updateTaskCategory: UpdateTaskCategory,
    private val updateTaskPriority: UpdateTaskPriority,
    private val updateTaskDueDate: UpdateTaskDueDate,
    private val coroutineDebouncer: CoroutineDebouncer,
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
    }.catch { emit(TaskDetailState.Error) }

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

    fun updateCategory(taskId: TaskId, categoryId: CategoryId) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskCategory(taskId = taskId.value, categoryId = categoryId.value)
        }
    }

    fun updatePriority(taskId: TaskId, priority: TaskPriority) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskPriority(taskId = taskId.value, priority = taskPriorityMapper.toDomain(priority))
        }
    }

    fun updateDueDate(taskId: TaskId, dueDate: LocalDateTime?) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskDueDate(taskId.value, dueDate)
        }
    }
}
