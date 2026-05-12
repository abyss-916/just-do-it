package com.escodro.task.presentation.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.taskwithcategory.CountUncompletedTasks
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.TaskPriorityMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.model.TaskPriority
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.datetime.LocalDateTime

/**
 * UI state emitted by [AddTaskViewModel].
 */
sealed class AddTaskState {
    data object None : AddTaskState()
    data object TaskLimitReached : AddTaskState()
    data object LongTermTaskExists : AddTaskState()
}

internal class AddTaskViewModel(
    private val addTaskUseCase: AddTask,
    private val alarmIntervalMapper: AlarmIntervalMapper,
    private val taskPriorityMapper: TaskPriorityMapper,
    private val applicationScope: AppCoroutineScope,
    private val countUncompletedTasks: CountUncompletedTasks,
    private val loadLongTermTask: LoadLongTermTask,
) : ViewModel() {

    var state: AddTaskState by mutableStateOf(AddTaskState.None)
        private set

    fun addTask(
        title: String,
        description: String? = null,
        categoryId: CategoryId?,
        dueDate: LocalDateTime?,
        alarmInterval: AlarmInterval = AlarmInterval.NEVER,
        priority: TaskPriority = TaskPriority.NONE,
        isLongTerm: Boolean = false,
    ) {
        if (title.isBlank()) return

        applicationScope.launch {
            if (isLongTerm) {
                val existing = loadLongTermTask()
                if (existing != null) {
                    state = AddTaskState.LongTermTaskExists
                    return@launch
                }
            } else {
                val count = countUncompletedTasks()
                if (count >= MaxTaskCount) {
                    state = AddTaskState.TaskLimitReached
                    return@launch
                }
            }

            val interval = alarmIntervalMapper.toDomain(alarmInterval)
            val taskPriority = taskPriorityMapper.toDomain(priority)
            val task = Task(
                title = title,
                description = description?.takeIf { it.isNotBlank() },
                dueDate = dueDate,
                categoryId = categoryId?.value,
                alarmInterval = interval,
                priority = taskPriority,
                isLongTerm = isLongTerm,
            )
            addTaskUseCase.invoke(task)
        }
    }

    fun consumeState() {
        state = AddTaskState.None
    }

    companion object {
        const val MaxTaskCount = 100
    }
}
