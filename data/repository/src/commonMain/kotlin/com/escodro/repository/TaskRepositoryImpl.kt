package com.escodro.repository

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.mapper.TaskMapper
import kotlinx.datetime.LocalDateTime

internal class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskMapper,
) : TaskRepository {
    override suspend fun insertTask(task: Task): Long =
        taskDataSource.insertTask(taskMapper.toRepo(task))

    override suspend fun updateTask(task: Task) {
        taskDataSource.updateTask(taskMapper.toRepo(task))
    }

    override suspend fun deleteTask(task: Task) {
        taskDataSource.deleteTask(taskMapper.toRepo(task))
    }

    override suspend fun cleanTable() {
        taskDataSource.cleanTable()
    }

    override suspend fun findAllTasksWithAlarmDate(): List<Task> =
        taskDataSource.findAllTasksWithAlarmDate().map { taskMapper.toDomain(it) }

    override suspend fun findTaskById(taskId: Long): Task? =
        taskDataSource.findTaskById(taskId)?.let { taskMapper.toDomain(it) }

    override suspend fun resetLongTermTaskIfCompletedBefore(thresholdDate: LocalDateTime) {
        taskDataSource.resetLongTermTaskIfCompletedBefore(thresholdDate)
    }
}
