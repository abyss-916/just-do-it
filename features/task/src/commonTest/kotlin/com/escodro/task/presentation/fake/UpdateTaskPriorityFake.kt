package com.escodro.task.presentation.fake

import com.escodro.domain.model.TaskPriority
import com.escodro.domain.usecase.task.UpdateTaskPriority

internal class UpdateTaskPriorityFake : UpdateTaskPriority {

    private val updatedMap = HashMap<Long, TaskPriority>()

    override suspend fun invoke(taskId: Long, priority: TaskPriority) {
        updatedMap[taskId] = priority
    }

    fun isPriorityUpdated(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getUpdatedPriority(taskId: Long): TaskPriority? =
        updatedMap[taskId]
}
