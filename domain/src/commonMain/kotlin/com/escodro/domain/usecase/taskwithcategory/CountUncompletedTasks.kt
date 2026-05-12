package com.escodro.domain.usecase.taskwithcategory

/**
 * Counts the number of uncompleted (non-long-term) tasks.
 */
fun interface CountUncompletedTasks {

    /**
     * Gets the count of uncompleted tasks.
     *
     * @return the number of uncompleted tasks
     */
    suspend operator fun invoke(): Int
}
