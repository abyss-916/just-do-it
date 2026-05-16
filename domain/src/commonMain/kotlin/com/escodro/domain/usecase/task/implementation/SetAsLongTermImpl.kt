package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.SetAsLongTerm
import com.escodro.domain.usecase.task.UnsetAsLongTerm
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class SetAsLongTermImpl(
    private val loadTask: LoadTask,
    private val loadLongTermTask: LoadLongTermTask,
    private val updateTask: UpdateTask,
    private val unsetAsLongTerm: UnsetAsLongTerm,
) : SetAsLongTerm {

    override suspend operator fun invoke(taskId: Long) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(isLongTerm = true)
        unsetAsLongTerm()
        updateTask(updatedTask)
    }
}
