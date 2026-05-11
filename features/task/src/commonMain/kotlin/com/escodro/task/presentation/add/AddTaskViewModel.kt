package com.escodro.task.presentation.add

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.mapper.TaskPriorityMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.model.TaskPriority
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.datetime.LocalDateTime

internal class AddTaskViewModel(
    private val addTaskUseCase: AddTask,
    private val alarmIntervalMapper: AlarmIntervalMapper,
    private val taskPriorityMapper: TaskPriorityMapper,
    private val applicationScope: AppCoroutineScope,
) : ViewModel() {

    fun addTask(
        title: String,
        description: String? = null,
        categoryId: CategoryId?,
        dueDate: LocalDateTime?,
        alarmInterval: AlarmInterval = AlarmInterval.NEVER,
        priority: TaskPriority = TaskPriority.NONE,
    ) {
        if (title.isBlank()) return

        val interval = alarmIntervalMapper.toDomain(alarmInterval)
        val taskPriority = taskPriorityMapper.toDomain(priority)
        applicationScope.launch {
            val task = Task(
                title = title,
                description = description?.takeIf { it.isNotBlank() },
                dueDate = dueDate,
                categoryId = categoryId?.value,
                alarmInterval = interval,
                priority = taskPriority,
            )
            addTaskUseCase.invoke(task)
        }
    }
}
