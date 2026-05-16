package com.escodro.task.presentation.category

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
actual fun CategoryFilterScrollbar(
    listState: LazyListState,
    modifier: Modifier,
) {
    val scrollbarStyle = ScrollbarStyle(
        minimalHeight = 8.dp,
        thickness = 4.dp,
        shape = RoundedCornerShape(2.dp),
        hoverDurationMillis = 300,
        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
    )

    HorizontalScrollbar(
        adapter = rememberScrollbarAdapter(listState),
        modifier = modifier,
        style = scrollbarStyle,
    )
}
