package com.escodro.task.presentation.fake

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class LoadLongTermTaskFake : LoadLongTermTask {

    var taskToBeReturned: TaskWithCategory? = null

    override suspend fun invoke(): TaskWithCategory? = taskToBeReturned
}
