package com.escodro.repository.mapper

import com.escodro.domain.model.TaskPriority as DomainPriority
import com.escodro.repository.model.TaskPriority as RepoPriority

/**
 * Maps TaskPriority between Repository and Domain.
 */
internal class TaskPriorityMapper {

    /**
     * Maps TaskPriority from Repo to Domain.
     */
    fun toDomain(repoPriority: RepoPriority): DomainPriority =
        when (repoPriority) {
            RepoPriority.NONE -> DomainPriority.NONE
            RepoPriority.LOW -> DomainPriority.LOW
            RepoPriority.MEDIUM -> DomainPriority.MEDIUM
            RepoPriority.HIGH -> DomainPriority.HIGH
        }

    /**
     * Maps TaskPriority from Domain to Repo.
     */
    fun toRepo(domainPriority: DomainPriority): RepoPriority =
        when (domainPriority) {
            DomainPriority.NONE -> RepoPriority.NONE
            DomainPriority.LOW -> RepoPriority.LOW
            DomainPriority.MEDIUM -> RepoPriority.MEDIUM
            DomainPriority.HIGH -> RepoPriority.HIGH
        }
}
