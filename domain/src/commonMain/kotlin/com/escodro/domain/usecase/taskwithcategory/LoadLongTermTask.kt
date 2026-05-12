package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory

/**
 * Loads the long-term task (if any).
 */
fun interface LoadLongTermTask {

    /**
     * Loads the current long-term task.
     *
     * @return the long-term task or null if none exists
     */
    suspend operator fun invoke(): TaskWithCategory?
}
