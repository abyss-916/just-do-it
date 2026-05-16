package com.escodro.tracker.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.content.AlkaaLoadingContent
import com.escodro.designsystem.components.content.DefaultIconTextContent
import com.escodro.designsystem.components.topbar.AlkaaToolbar
import com.escodro.resources.Res
import com.escodro.resources.tracker_cd_empty
import com.escodro.resources.tracker_cd_error
import com.escodro.resources.tracker_header_empty
import com.escodro.resources.tracker_header_error
import com.escodro.tracker.model.Tracker
import com.escodro.tracker.presentation.components.TaskGraph
import com.escodro.tracker.presentation.components.TaskTrackerList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun TrackerScreen(onUpPress: () -> Unit) {
    TrackerLoader(onUpPress = onUpPress)
}

@Composable
internal fun TrackerLoader(viewModel: TrackerViewModel = koinInject(), onUpPress: () -> Unit) {
    val data by remember {
        viewModel.loadTracker()
    }.collectAsState(initial = TrackerViewState.Loading)

    Scaffold(topBar = {
        AlkaaToolbar(
            isSinglePane = true,
            onUpPress = onUpPress,
        )
    }) { paddingValues ->
        Crossfade(targetState = data, modifier = Modifier.padding(paddingValues)) { state ->
            when (state) {
                TrackerViewState.Empty -> TrackerEmpty()
                is TrackerViewState.Error -> TrackerError()
                is TrackerViewState.Loaded -> TrackerLoadedContent(state.trackerInfo)
                TrackerViewState.Loading -> AlkaaLoadingContent()
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun TrackerLoadedContent(trackerInfo: Tracker.Info) {
    val categoryList = trackerInfo.categoryInfoList
    Column {
        TaskGraph(
            list = categoryList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(24.dp),
        )
        TaskTrackerList(
            list = categoryList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(16.dp),
        )
    }
}

@Composable
private fun TrackerEmpty() {
    DefaultIconTextContent(
        icon = Icons.Outlined.DataUsage,
        iconContentDescription = stringResource(Res.string.tracker_cd_empty),
        header = stringResource(Res.string.tracker_header_empty),
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun TrackerError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = stringResource(Res.string.tracker_cd_error),
        header = stringResource(Res.string.tracker_header_error),
        modifier = Modifier.padding(16.dp),
    )
}

@Preview(showBackground = true)
@Composable
private fun TrackerLoadedContentPreview() {
    val sampleData = Tracker.Info(
        categoryInfoList = List(5) { index ->
            Tracker.CategoryInfo(
                name = "Category $index",
                color = 0xFFFF0000.toInt(), // Red in ARGB
                taskCount = (index + 1) * 2,
                percentage = ((index + 1) * 10).toFloat(),
            )
        }.toImmutableList(),
    )
    TrackerLoadedContent(trackerInfo = sampleData)
}
