package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.AddLongTermTask
import com.escodro.domain.usecase.taskwithcategory.LoadLongTermTask

internal class AddLongTermTaskImpl(
    private val taskRepository: TaskRepository,
    private val loadLongTermTask: LoadLongTermTask,
) : AddLongTermTask {

    override suspend operator fun invoke(task: Task): Boolean {
        if (task.title.isBlank()) return false

        val existing = loadLongTermTask()
        if (existing != null) return false

        val taskToInsert = task.copy(
            isLongTerm = true,
            description = task.description?.takeIf { it.isNotBlank() },
        )
        taskRepository.insertTask(taskToInsert)
        return true
    }
}
