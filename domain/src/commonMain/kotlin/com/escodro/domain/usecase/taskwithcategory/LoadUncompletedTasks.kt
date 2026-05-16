package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get all uncompleted tasks from the database.
 */
interface LoadUncompletedTasks {

    /**
     * Gets all uncompleted tasks.
     *
     * @param categoryId the category id to filter by, or null for all
     */
    operator fun invoke(categoryId: Long? = null): Flow<List<TaskWithCategory>>
}
