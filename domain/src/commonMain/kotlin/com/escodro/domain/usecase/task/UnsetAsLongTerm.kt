package com.escodro.domain.usecase.task

/**
 * Unsets the long-term task (if any).
 */
fun interface UnsetAsLongTerm {

    /**
     * Unsets the current long-term task.
     */
    suspend operator fun invoke()
}
