package com.escodro.task.presentation.list

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TaskListScrollbar(
    listState: LazyListState,
    modifier: Modifier,
) {
    // iOS provides native scroll indicators
}
