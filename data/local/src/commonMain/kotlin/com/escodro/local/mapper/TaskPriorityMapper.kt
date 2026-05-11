package com.escodro.local.mapper

import com.escodro.local.model.TaskPriority as LocalPriority
import com.escodro.repository.model.TaskPriority as RepoPriority

/**
 * Maps TaskPriority between Repository and Local.
 */
internal class TaskPriorityMapper {

    /**
     * Maps TaskPriority from Local to Repo.
     */
    fun toRepo(localPriority: LocalPriority): RepoPriority =
        when (localPriority) {
            LocalPriority.NONE -> RepoPriority.NONE
            LocalPriority.LOW -> RepoPriority.LOW
            LocalPriority.MEDIUM -> RepoPriority.MEDIUM
            LocalPriority.HIGH -> RepoPriority.HIGH
        }

    /**
     * Maps TaskPriority from Repo to Local.
     */
    fun toLocal(repoPriority: RepoPriority): LocalPriority =
        when (repoPriority) {
            RepoPriority.NONE -> LocalPriority.NONE
            RepoPriority.LOW -> LocalPriority.LOW
            RepoPriority.MEDIUM -> LocalPriority.MEDIUM
            RepoPriority.HIGH -> LocalPriority.HIGH
        }
}
