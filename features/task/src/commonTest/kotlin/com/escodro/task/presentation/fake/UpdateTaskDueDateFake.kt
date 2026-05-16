package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.task.UpdateTaskDueDate
import kotlinx.datetime.LocalDateTime

internal class UpdateTaskDueDateFake : UpdateTaskDueDate {

    private val updatedMap = HashMap<Long, LocalDateTime?>()

    override suspend fun invoke(taskId: Long, dueDate: LocalDateTime?) {
        updatedMap[taskId] = dueDate
    }

    fun isDueDateUpdated(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getUpdatedDueDate(taskId: Long): LocalDateTime? =
        updatedMap[taskId]
}
