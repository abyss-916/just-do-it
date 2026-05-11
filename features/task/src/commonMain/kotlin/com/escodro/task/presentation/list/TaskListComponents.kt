package com.escodro.task.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.priority_high
import com.escodro.designsystem.theme.priority_low
import com.escodro.designsystem.theme.priority_medium
import com.escodro.task.model.TaskPriority
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.provider.RelativeDateTimeProvider
import kotlinx.datetime.LocalDateTime
import org.koin.compose.koinInject

/**
 * Alkaa Task Item.
 *
 * @param task the task item to be rendered
 * @param onItemClick the action to be done when the item is clicked
 * @param onCheckedChange action to be called when the checked value changes
 * @param modifier the decorator
 */
@Composable
internal fun TaskItem(
    task: TaskWithCategory,
    onItemClick: (Long) -> Unit,
    onCheckedChange: (TaskWithCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .height(74.dp)
            .clickable { onItemClick(task.task.id) },
    ) {
        Row {
            CardRibbon(colorInt = task.category?.color)
            task.task.priority?.takeIf { it != TaskPriority.NONE }?.let { priority ->
                PriorityDot(priority = priority)
            }
            RadioButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics { checkboxName = task.task.title },
                selected = task.task.isCompleted,
                onClick = { onCheckedChange(task) },
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = task.task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                task.task.dueDate?.let { calendar ->
                    RelativeDateText(calendar = calendar)
                }
            }
        }
    }
}

@Composable
internal fun CardRibbon(colorInt: Int?, modifier: Modifier = Modifier) {
    val ribbonColor = if (colorInt != null) {
        Color(colorInt)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Spacer(
        modifier
            .width(18.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .background(ribbonColor),
    )
}

@Composable
internal fun RelativeDateText(
    calendar: LocalDateTime,
    relativeDateTimeProvider: RelativeDateTimeProvider = koinInject(),
) {
    Text(
        text = relativeDateTimeProvider.toRelativeDateTimeString(calendar),
        style = MaterialTheme.typography.bodyMedium,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

/**
 * Semantics key for Checkbox naming.
 */
val CheckboxNameKey = SemanticsPropertyKey<String>("Checkbox")
private var SemanticsPropertyReceiver.checkboxName by CheckboxNameKey

@Composable
private fun PriorityDot(priority: TaskPriority) {
    val dotColor = when (priority) {
        TaskPriority.HIGH -> priority_high
        TaskPriority.MEDIUM -> priority_medium
        TaskPriority.LOW -> priority_low
        TaskPriority.NONE -> Color.Unspecified
    }
    Box(
        modifier = Modifier
            .size(8.dp)
            .alignBy { it.height / 2 }
            .padding(horizontal = 4.dp)
            .background(dotColor, CircleShape),
    )
}
