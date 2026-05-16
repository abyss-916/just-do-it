package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.usecase.task.UnsetAsLongTerm
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class UnsetAsLongTermImpl(
    private val loadLongTermTask: LoadLongTermTask,
    private val updateTask: UpdateTask,
) : UnsetAsLongTerm {

    override suspend operator fun invoke() {
        val taskWithCategory = loadLongTermTask() ?: return
        val updatedTask = taskWithCategory.task.copy(isLongTerm = false)
        updateTask(updatedTask)
    }
}
