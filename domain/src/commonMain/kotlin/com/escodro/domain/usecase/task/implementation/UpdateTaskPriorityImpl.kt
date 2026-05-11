package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.model.TaskPriority
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskPriority

internal class UpdateTaskPriorityImpl(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask,
) : UpdateTaskPriority {

    override suspend fun invoke(taskId: Long, priority: TaskPriority) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(priority = priority)
        updateTask(updatedTask)
    }
}
