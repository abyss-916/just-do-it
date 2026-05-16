package com.escodro.domain.repository

import com.escodro.domain.model.Task
import kotlinx.datetime.LocalDateTime

/**
 * Interface to represent the implementation of Task repository.
 */
interface TaskRepository {

    /**
     * Inserts a new task.
     *
     * @param task task to be added
     */
    suspend fun insertTask(task: Task): Long

    /**
     * Updates a task.
     *
     * @param task task to be updated
     */
    suspend fun updateTask(task: Task)

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     */
    suspend fun deleteTask(task: Task)

    /**
     * Cleans the entire table.
     */
    suspend fun cleanTable()

    /**
     * Get all inserted tasks with alarm date.
     *
     * @return all inserted tasks with alarm date
     */
    suspend fun findAllTasksWithAlarmDate(): List<Task>

    /**
     * Get task by id.
     *
     * @param taskId task id
     *
     * @return selected task
     */
    suspend fun findTaskById(taskId: Long): Task?

    /**
     * Resets the long-term task if it was completed before the given date.
     *
     * @param thresholdDate the threshold date to compare against
     */
    suspend fun resetLongTermTaskIfCompletedBefore(thresholdDate: LocalDateTime)
}
