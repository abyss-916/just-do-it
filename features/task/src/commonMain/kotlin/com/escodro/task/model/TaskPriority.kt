package com.escodro.task.model

import com.escodro.resources.Res
import com.escodro.resources.task_priority_high
import com.escodro.resources.task_priority_low
import com.escodro.resources.task_priority_medium
import com.escodro.resources.task_priority_none
import org.jetbrains.compose.resources.StringResource

/**
 * Represents the priority level of a task for display.
 *
 * @property index the index for sorting
 * @property title the string resource for display
 */
@Suppress("MagicNumber")
enum class TaskPriority(val index: Int?, val title: StringResource) {

    /**
     * Represents no priority.
     */
    NONE(index = 0, title = Res.string.task_priority_none),

    /**
     * Represents low priority.
     */
    LOW(index = 1, title = Res.string.task_priority_low),

    /**
     * Represents medium priority.
     */
    MEDIUM(index = 2, title = Res.string.task_priority_medium),

    /**
     * Represents high priority.
     */
    HIGH(index = 3, title = Res.string.task_priority_high),
}
