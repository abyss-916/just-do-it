package com.escodro.domain.usecase.task

import kotlinx.datetime.LocalDateTime

/**
 * Use case to update a task due date.
 */
interface UpdateTaskDueDate {

    /**
     * Updates a task due date.
     *
     * @param taskId the task id to be updated
     * @param dueDate the due date to be set
     */
    suspend operator fun invoke(taskId: Long, dueDate: LocalDateTime?)
}
