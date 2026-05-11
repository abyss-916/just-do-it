package com.escodro.domain.usecase.task

import com.escodro.domain.model.TaskPriority

/**
 * Use case to update the priority of a task.
 */
interface UpdateTaskPriority {

    /**
     * Updates the priority of a task.
     *
     * @param taskId the task id to be updated
     * @param priority the priority to be set
     */
    suspend operator fun invoke(taskId: Long, priority: TaskPriority)
}
