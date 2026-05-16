package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task

/**
 * Updates an existing long-term task.
 */
fun interface UpdateLongTermTask {

    /**
     * Updates the given long-term task.
     *
     * @param task the task to update
     */
    suspend operator fun invoke(task: Task)
}
