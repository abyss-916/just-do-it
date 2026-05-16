package com.escodro.local.dao

import com.escodro.local.Task

/**
 * DAO class to handle all [Task]-related database operations.
 */
interface TaskDao {

    /**
     * Inserts a new task.
     *
     * @param task task to be added
     *
     * @return the id of the inserted task
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
    suspend fun getTaskById(taskId: Long): Task?

    /**
     * Get the long-term task (if any).
     *
     * @return the long-term task or null
     */
    suspend fun findLongTermTask(): Task?

    /**
     * Resets the long-term task if it was completed before the given date.
     *
     * @param thresholdDate the threshold date to compare against
     */
    suspend fun resetLongTermTaskIfCompletedBefore(thresholdDate: kotlinx.datetime.LocalDateTime)
}
