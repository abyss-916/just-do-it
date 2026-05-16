package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.task.ResetLongTermTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class LoadLongTermTaskImpl(
    private val repository: TaskWithCategoryRepository,
    private val resetLongTermTask: ResetLongTermTask,
    private val dateTimeProvider: DateTimeProvider,
) : LoadLongTermTask {

    override suspend operator fun invoke(): TaskWithCategory? {
        val today = dateTimeProvider.getCurrentInstant()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        resetLongTermTask(LocalDateTime(today, LocalTime(0, 0)))
        return repository.findLongTermTask()
    }
}
