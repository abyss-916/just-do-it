package com.escodro.task.presentation.category

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun CategoryFilterScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
)
