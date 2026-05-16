package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.UpdateLongTermTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class UpdateLongTermTaskImpl(
    private val taskRepository: TaskRepository,
    private val loadLongTermTask: LoadLongTermTask,
) : UpdateLongTermTask {

    override suspend operator fun invoke(task: Task) {
        val existing = loadLongTermTask() ?: return
        val taskToUpdate = task.copy(
            id = existing.task.id,
            isLongTerm = true,
        )
        taskRepository.updateTask(taskToUpdate)
    }
}
