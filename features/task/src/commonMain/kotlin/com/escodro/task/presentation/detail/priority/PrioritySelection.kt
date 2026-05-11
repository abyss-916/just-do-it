package com.escodro.task.presentation.detail.priority

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.priority_high
import com.escodro.designsystem.theme.priority_low
import com.escodro.designsystem.theme.priority_medium
import com.escodro.resources.Res
import com.escodro.resources.task_priority_high
import com.escodro.resources.task_priority_low
import com.escodro.resources.task_priority_medium
import com.escodro.resources.task_priority_none
import com.escodro.task.model.TaskPriority
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PrioritySelection(
    currentPriority: TaskPriority,
    onPriorityChange: (TaskPriority) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val priorities = TaskPriority.entries.toImmutableList()

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(
            items = priorities,
            itemContent = { priority ->
                PriorityChip(
                    priority = priority,
                    isSelected = priority == currentPriority,
                    onSelect = { onPriorityChange(priority) },
                )
            },
        )
    }
}

@Composable
private fun PriorityChip(
    priority: TaskPriority,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    val selectedColor = priorityColor(priority)
    FilterChip(
        selected = isSelected,
        onClick = onSelect,
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(selectedColor, CircleShape),
                )
                Text(
                    text = stringResource(priority.title),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        },
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = selectedColor,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@Composable
private fun priorityColor(priority: TaskPriority): Color =
    when (priority) {
        TaskPriority.HIGH -> priority_high
        TaskPriority.MEDIUM -> priority_medium
        TaskPriority.LOW -> priority_low
        TaskPriority.NONE -> MaterialTheme.colorScheme.outline
    }
