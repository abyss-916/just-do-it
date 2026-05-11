package com.escodro.local.model

/**
 * Represents the priority level of a task.
 *
 * @property id the id representation of the priority
 */
@Suppress("MagicNumber")
enum class TaskPriority(val id: Int) {

    /**
     * Represents no priority.
     */
    NONE(0),

    /**
     * Represents low priority.
     */
    LOW(1),

    /**
     * Represents medium priority.
     */
    MEDIUM(2),

    /**
     * Represents high priority.
     */
    HIGH(3),
}
