package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskDueDate
import kotlinx.datetime.LocalDateTime

internal class UpdateTaskDueDateImpl(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask,
) : UpdateTaskDueDate {

    override suspend fun invoke(taskId: Long, dueDate: LocalDateTime?) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(dueDate = dueDate)
        updateTask(updatedTask)
    }
}
