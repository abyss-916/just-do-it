package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to set a task as uncompleted in the database.
 */
class UncompleteTask(private val taskRepository: TaskRepository) {

    /**
     * Sets the given task as uncompleted.
     *
     * @param task the task to be updated
     */
    suspend operator fun invoke(task: Task) {
        val updatedTask = updateTaskAsUncompleted(task)
        taskRepository.updateTask(updatedTask)
    }

    private fun updateTaskAsUncompleted(task: Task) =
        task.copy(isCompleted = false, completedDate = null)
}
