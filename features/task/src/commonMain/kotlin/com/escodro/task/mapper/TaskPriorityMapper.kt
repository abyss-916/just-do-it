package com.escodro.task.mapper

import com.escodro.domain.model.TaskPriority as DomainPriority
import com.escodro.task.model.TaskPriority as ViewPriority

/**
 * Maps TaskPriority between Domain and View.
 */
internal class TaskPriorityMapper {

    /**
     * Maps TaskPriority from Domain to View.
     */
    fun toViewData(domainPriority: DomainPriority?): ViewPriority =
        when (domainPriority) {
            DomainPriority.NONE, null -> ViewPriority.NONE
            DomainPriority.LOW -> ViewPriority.LOW
            DomainPriority.MEDIUM -> ViewPriority.MEDIUM
            DomainPriority.HIGH -> ViewPriority.HIGH
        }

    /**
     * Maps TaskPriority from View to Domain.
     */
    fun toDomain(viewPriority: ViewPriority): DomainPriority =
        when (viewPriority) {
            ViewPriority.NONE -> DomainPriority.NONE
            ViewPriority.LOW -> DomainPriority.LOW
            ViewPriority.MEDIUM -> DomainPriority.MEDIUM
            ViewPriority.HIGH -> DomainPriority.HIGH
        }
}
