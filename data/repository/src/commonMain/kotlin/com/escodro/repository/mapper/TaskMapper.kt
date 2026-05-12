package com.escodro.repository.mapper

import com.escodro.domain.model.Task as DomainTask
import com.escodro.repository.model.Task as RepoTask

/**
 * Maps Tasks between Repository and Domain.
 */
internal class TaskMapper(
    private val alarmIntervalMapper: AlarmIntervalMapper,
    private val taskPriorityMapper: TaskPriorityMapper,
) {

    /**
     * Maps Task from Repo to Domain.
     *
     * @param repoTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toDomain(repoTask: RepoTask): DomainTask =
        DomainTask(
            id = repoTask.id,
            isCompleted = repoTask.isCompleted,
            title = repoTask.title,
            description = repoTask.description,
            categoryId = repoTask.categoryId,
            dueDate = repoTask.dueDate,
            creationDate = repoTask.creationDate,
            completedDate = repoTask.completedDate,
            isRepeating = repoTask.isRepeating,
            alarmInterval = repoTask.alarmInterval?.let { alarmIntervalMapper.toDomain(it) },
            priority = repoTask.priority?.let { taskPriorityMapper.toDomain(it) }
                ?: com.escodro.domain.model.TaskPriority.NONE,
            isLongTerm = repoTask.isLongTerm,
        )

    /**
     * Maps Task from Repo to Domain.
     *
     * @param repoTaskList the list of Task to be converted.
     *
     * @return the converted list of Task
     */
    fun toDomain(repoTaskList: List<RepoTask>): List<DomainTask> =
        repoTaskList.map { toDomain(it) }

    /**
     * Maps Task from Domain to Repo.
     *
     * @param domainTaskList the list of Task to be converted.
     *
     * @return the converted list of Task
     */
    fun toRepo(domainTaskList: List<DomainTask>): List<RepoTask> =
        domainTaskList.map { toRepo(it) }

    /**
     * Maps Task from Domain to Repo.
     *
     * @param domainTask the Task to be converted.
     *
     * @return the converted Task
     */
    fun toRepo(domainTask: DomainTask): RepoTask =
        RepoTask(
            id = domainTask.id,
            isCompleted = domainTask.isCompleted,
            title = domainTask.title,
            description = domainTask.description,
            categoryId = domainTask.categoryId,
            dueDate = domainTask.dueDate,
            creationDate = domainTask.creationDate,
            completedDate = domainTask.completedDate,
            isRepeating = domainTask.isRepeating,
            alarmInterval = domainTask.alarmInterval?.let { alarmIntervalMapper.toRepo(it) },
            priority = domainTask.priority?.let { taskPriorityMapper.toRepo(it) }
                ?: com.escodro.repository.model.TaskPriority.NONE,
            isLongTerm = domainTask.isLongTerm,
        )
}
