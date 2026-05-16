package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task

/**
 * Adds a new long-term task.
 */
fun interface AddLongTermTask {

    /**
     * Adds a new long-term task. Fails if a long-term task already exists.
     *
     * @param task the task to add
     * @return true if added successfully, false if a long-term task already exists
     */
    suspend operator fun invoke(task: Task): Boolean
}
