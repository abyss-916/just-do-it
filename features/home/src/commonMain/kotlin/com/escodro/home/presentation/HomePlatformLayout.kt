package com.escodro.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.escodro.appstate.AlkaaAppState
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.collections.immutable.ImmutableList

@Composable
expect fun HomePlatformLayout(
    appState: AlkaaAppState,
    navItems: ImmutableList<TopLevel>,
    currentSection: TopLevel,
    setCurrentSection: (TopLevel) -> Unit,
    modifier: Modifier = Modifier,
)
