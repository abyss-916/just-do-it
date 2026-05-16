package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task

/**
 * Use case to update a task from the database.
 */
interface UpdateTask {

    /**
     * Updates a task.
     *
     * @param task the task to be updated
     */
    suspend operator fun invoke(task: Task)
}
