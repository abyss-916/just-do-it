package com.escodro.domain.usecase.tracker.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

/**
 * Use case to get completed tasks in Tracker format for the last 30 days from the database.
 */
@OptIn(ExperimentalTime::class)
internal class LoadCompletedTasksByPeriodImpl(
    private val repository: TaskWithCategoryRepository,
    private val dateTimeProvider: DateTimeProvider,
) : LoadCompletedTasksByPeriod {

    /**
     * Gets completed tasks in Tracker format for the last 30 days.
     */
    override operator fun invoke(): Flow<List<TaskWithCategory>> =
        repository
            .findAllTasksWithCategory()
            .map { list ->
                list
                    .filter { item -> item.task.isCompleted }
                    .filter(::filterByLastMonth)
            }

    private fun filterByLastMonth(task: TaskWithCategory): Boolean {
        val lastMonth = dateTimeProvider.getCurrentInstant().minus(LAST_30_DAYS.days)
        val taskDate =
            task.task.completedDate?.toInstant(TimeZone.currentSystemDefault()) ?: return false
        return taskDate > lastMonth
    }

    companion object {
        private const val LAST_30_DAYS = 30
    }
}
