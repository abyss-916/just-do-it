package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.CountUncompletedTasks
import kotlinx.coroutines.flow.first

internal class CountUncompletedTasksImpl(
    private val repository: TaskWithCategoryRepository,
) : CountUncompletedTasks {

    override suspend operator fun invoke(): Int =
        repository.findAllTasksWithCategory().first()
            .filterNot { item -> item.task.isCompleted || item.task.isLongTerm }
            .size
}
