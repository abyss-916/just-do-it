package com.escodro.domain.usecase.task

/**
 * Deletes the current long-term task.
 */
fun interface DeleteLongTermTask {

    /**
     * Deletes the current long-term task (if any).
     */
    suspend operator fun invoke()
}
