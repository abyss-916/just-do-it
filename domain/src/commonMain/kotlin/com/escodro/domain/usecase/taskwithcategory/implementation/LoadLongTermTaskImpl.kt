package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class LoadLongTermTaskImpl(
    private val repository: TaskWithCategoryRepository,
) : LoadLongTermTask {

    override suspend operator fun invoke(): TaskWithCategory? =
        repository.findLongTermTask()
}
