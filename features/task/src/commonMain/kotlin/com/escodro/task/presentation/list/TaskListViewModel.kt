package com.escodro.task.presentation.list

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * ViewModel responsible to handle the interaction between the presentation and business logic from
 * Task Section.
 */
internal class TaskListViewModel(
    private val loadAllTasksUseCase: LoadUncompletedTasks,
    private val loadLongTermTaskUseCase: LoadLongTermTask,
    private val updateTaskStatusUseCase: UpdateTaskStatus,
    private val applicationScope: AppCoroutineScope,
    private val taskWithCategoryMapper: TaskWithCategoryMapper,
) : ViewModel() {

    fun loadTaskList(categoryId: Long? = null): Flow<TaskListViewState> = flow {
        loadAllTasksUseCase(categoryId = categoryId)
            .map { task -> taskWithCategoryMapper.toView(task) }
            .catch { error -> emit(TaskListViewState.Error(error)) }
            .collect { tasks ->
                val state = if (tasks.isNotEmpty()) {
                    TaskListViewState.Loaded(tasks)
                } else {
                    TaskListViewState.Empty
                }
                emit(state)
            }
    }

    fun loadLongTermTask(): Flow<TaskWithCategory?> = flow {
        val task = loadLongTermTaskUseCase()
        val result = if (task != null) {
            taskWithCategoryMapper.toView(listOf(task)).firstOrNull()
        } else {
            null
        }
        emit(result)
    }

    fun updateTaskStatus(item: TaskWithCategory) = applicationScope.launch {
        updateTaskStatusUseCase(item.task.id)
    }
}
