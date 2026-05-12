package com.escodro.domain.usecase.task

/**
 * Sets a task as the long-term task, unsetting any previous one.
 * Only one task can be marked as long-term at a time.
 */
fun interface SetAsLongTerm {

    /**
     * Sets the given task as the long-term task.
     *
     * @param taskId the task id to mark as long-term
     */
    suspend operator fun invoke(taskId: Long)
}
