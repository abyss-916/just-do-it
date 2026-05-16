package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.DeleteLongTermTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class DeleteLongTermTaskImpl(
    private val taskRepository: TaskRepository,
    private val loadLongTermTask: LoadLongTermTask,
) : DeleteLongTermTask {

    override suspend operator fun invoke() {
        val existing = loadLongTermTask() ?: return
        taskRepository.deleteTask(existing.task)
    }
}
