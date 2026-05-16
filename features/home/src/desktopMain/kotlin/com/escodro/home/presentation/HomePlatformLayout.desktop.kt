package com.escodro.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.escodro.appstate.AlkaaAppState
import com.escodro.designsystem.animation.TopBarEnterTransition
import com.escodro.designsystem.animation.TopBarExitTransition
import com.escodro.designsystem.components.topbar.MainTopBar
import com.escodro.navigation.compose.Navigation
import com.escodro.navigationapi.marker.TopLevel
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun HomePlatformLayout(
    appState: AlkaaAppState,
    navItems: ImmutableList<TopLevel>,
    currentSection: TopLevel,
    setCurrentSection: (TopLevel) -> Unit,
    modifier: Modifier,
) {
    val isTopAppBarVisible = appState.navBackStack.isTopBarVisible

    Column(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = isTopAppBarVisible,
            enter = TopBarEnterTransition,
            exit = TopBarExitTransition,
        ) {
            MainTopBar(
                title = stringResource(currentSection.title),
                modifier = Modifier.HomeTopBarDivider(),
            )
        }

        NavigationSuiteScaffold(
            navigationSuiteItems = {
                desktopAlkaaBottomNav(
                    items = navItems,
                    currentSection = currentSection,
                    setCurrentSection = setCurrentSection,
                )
            },
        ) {
            Scaffold(
                topBar = { },
                contentWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0),
                content = { paddingValues ->
                    Navigation(
                        navBackStack = appState.navBackStack,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                                top = if (paddingValues.calculateTopPadding() > 0.dp) paddingValues.calculateTopPadding() else 0.dp,
                                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                                bottom = paddingValues.calculateBottomPadding(),
                            )
                            .consumeWindowInsets(paddingValues)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                            ),
                    )
                },
            )
        }
    }
}

private fun NavigationSuiteScope.desktopAlkaaBottomNav(
    items: ImmutableList<TopLevel>,
    currentSection: TopLevel,
    setCurrentSection: (TopLevel) -> Unit,
) {
    items.forEach { section ->
        val isSelected = section == currentSection
        val title = section.title
        item(
            selected = isSelected,
            onClick = { setCurrentSection(section) },
            icon = {
                Icon(
                    imageVector = section.icon,
                    contentDescription = stringResource(title),
                )
            },
            label = {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            modifier = Modifier.testTag(title.toString()),
        )
    }
}
