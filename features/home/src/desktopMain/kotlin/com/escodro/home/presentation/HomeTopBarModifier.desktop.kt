package com.escodro.home.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

@Suppress("FunctionName")
@Composable
actual fun Modifier.HomeTopBarDivider(): Modifier = this
    .fillMaxWidth()
    .composed {
        val dividerColor = MaterialTheme.colorScheme.outlineVariant
        drawBehind {
            drawLine(
                color = dividerColor,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx(),
            )
        }
    }
