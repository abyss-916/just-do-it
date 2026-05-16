package com.escodro.task.presentation.longterm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddLongTermTask
import com.escodro.domain.usecase.task.DeleteLongTermTask
import com.escodro.domain.usecase.task.UpdateLongTermTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

sealed class LongTermTaskState {
    data object Loading : LongTermTaskState()

    data object Empty : LongTermTaskState()

    data class Loaded(val task: com.escodro.task.model.Task) : LongTermTaskState()

    data object Error : LongTermTaskState()
}

internal class LongTermTaskViewModel(
    private val loadLongTermTaskUseCase: LoadLongTermTask,
    private val addLongTermTask: AddLongTermTask,
    private val updateLongTermTask: UpdateLongTermTask,
    private val deleteLongTermTask: DeleteLongTermTask,
    private val taskMapper: TaskMapper,
    private val applicationScope: AppCoroutineScope,
) : ViewModel() {

    companion object {
        var refreshKey by mutableStateOf(0)
            private set

        fun triggerRefresh() {
            refreshKey += 1
        }
    }

    fun loadLongTermTask(): Flow<LongTermTaskState> = flow {
        emit(LongTermTaskState.Loading)
        val taskWithCategory = loadLongTermTaskUseCase()
        if (taskWithCategory != null) {
            val viewTask = taskMapper.toView(taskWithCategory.task)
            emit(LongTermTaskState.Loaded(viewTask))
        } else {
            emit(LongTermTaskState.Empty)
        }
    }
        .catch { emit(LongTermTaskState.Error) }

    suspend fun addTask(
        title: String,
        description: String? = null,
    ) {
        if (title.isBlank()) return
        val domainTask = Task(
            title = title,
            description = description?.takeIf { it.isNotBlank() },
        )
        addLongTermTask(domainTask)
        triggerRefresh()
    }

    suspend fun updateTask(
        id: Long,
        title: String,
        description: String? = null,
    ) {
        if (title.isBlank()) return
        val existing = loadLongTermTaskUseCase() ?: return
        val domainTask = Task(
            id = id,
            title = title,
            description = description?.takeIf { it.isNotBlank() },
            isLongTerm = true,
            isCompleted = existing.task.isCompleted,
        )
        updateLongTermTask(domainTask)
        triggerRefresh()
    }

    suspend fun deleteTask() {
        deleteLongTermTask()
        triggerRefresh()
    }
}
