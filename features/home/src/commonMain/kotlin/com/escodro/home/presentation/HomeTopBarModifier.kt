package com.escodro.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Returns a platform-specific modifier applied to the home top bar.
 * Desktop adds a bottom divider; mobile returns the original modifier.
 */
@Suppress("FunctionName")
@Composable
expect fun Modifier.HomeTopBarDivider(): Modifier
