package com.escodro.domain.usecase.tracker

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get completed tasks in Tracker format for the last 30 days from the database.
 */
interface LoadCompletedTasksByPeriod {

    /**
     * Gets completed tasks in Tracker format for the last 30 days.
     */
    operator fun invoke(): Flow<List<TaskWithCategory>>
}
