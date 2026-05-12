package com.escodro.task.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.components.button.AddFloatingButton
import com.escodro.designsystem.components.content.AlkaaLoadingContent
import com.escodro.designsystem.components.content.DefaultIconTextContent
import com.escodro.resources.Res
import com.escodro.resources.task_cd_add_task
import com.escodro.resources.task_detail_pane_title
import com.escodro.resources.task_list_cd_empty_list
import com.escodro.resources.task_list_cd_error
import com.escodro.resources.task_list_header_empty
import com.escodro.resources.task_list_header_error
import com.escodro.resources.task_list_header_long_term
import com.escodro.resources.task_snackbar_button_undo
import com.escodro.resources.task_snackbar_message_complete
import com.escodro.task.model.TaskWithCategory
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.detail.main.TaskDetailScreen
import com.escodro.task.presentation.detail.main.TaskId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Task Section.
 */
@Composable
fun TaskListSection(
    isSinglePane: Boolean,
    onItemClick: (Long) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TaskListLoader(
        isSinglePane = isSinglePane,
        modifier = modifier,
        onFabClick = onFabClick,
        onItemClick = onItemClick,
    )
}

@Suppress("LongParameterList")
@Composable
internal fun TaskListLoader(
    isSinglePane: Boolean,
    onItemClick: (Long) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
    taskListViewModel: TaskListViewModel = koinInject(),
    categoryViewModel: CategoryListViewModel = koinInject(),
) {
    val (currentCategory, onCategoryChange) = rememberSaveable { mutableStateOf<CategoryId?>(null) }
    val refreshKey: Int by rememberRefreshKey()

    val taskViewState by remember(taskListViewModel, currentCategory, refreshKey) {
        taskListViewModel.loadTaskList(currentCategory?.value)
    }.collectAsState(initial = TaskListViewState.Loading)

    val longTermTask by remember(taskListViewModel, refreshKey) {
        taskListViewModel.loadLongTermTask()
    }.collectAsState(initial = null)

    val categoryViewState by remember(categoryViewModel) {
        categoryViewModel.loadCategories()
    }.collectAsState(initial = CategoryState.Loading)

    if (isSinglePane) {
        TaskListScaffold(
            taskViewState = taskViewState,
            longTermTask = longTermTask,
            categoryViewState = categoryViewState,
            onTaskCheckedChange = taskListViewModel::updateTaskStatus,
            onFabClick = onFabClick,
            currentCategory = currentCategory,
            onCategoryChange = onCategoryChange,
            refreshKey = refreshKey,
            modifier = modifier,
            onItemClick = onItemClick,
        )
    } else {
        AdaptiveTaskListScaffold(
            taskViewState = taskViewState,
            longTermTask = longTermTask,
            categoryViewState = categoryViewState,
            onUpdateTaskStatus = taskListViewModel::updateTaskStatus,
            onFabClick = onFabClick,
            currentCategory = currentCategory,
            onCategoryChange = onCategoryChange,
            refreshKey = refreshKey,
            modifier = modifier,
        )
    }
}

@Suppress("LongParameterList")
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AdaptiveTaskListScaffold(
    taskViewState: TaskListViewState,
    longTermTask: TaskWithCategory?,
    categoryViewState: CategoryState,
    onUpdateTaskStatus: (TaskWithCategory) -> Unit,
    onFabClick: () -> Unit,
    currentCategory: CategoryId?,
    onCategoryChange: (CategoryId?) -> Unit,
    refreshKey: Int,
    modifier: Modifier = Modifier,
) {
    val navigator: ThreePaneScaffoldNavigator<TaskId> =
        rememberListDetailPaneScaffoldNavigator<TaskId>()
    val coroutineScope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                TaskListScaffold(
                    taskViewState = taskViewState,
                    longTermTask = longTermTask,
                    categoryViewState = categoryViewState,
                    onTaskCheckedChange = { item ->
                        onUpdateTaskStatus(item)
                        coroutineScope.launch { navigator.navigateBack() }
                    },
                    onFabClick = onFabClick,
                    currentCategory = currentCategory,
                    onCategoryChange = onCategoryChange,
                    modifier = modifier,
                    refreshKey = refreshKey,
                    onItemClick = { taskId ->
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, TaskId(taskId))
                        }
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val taskId = navigator.currentDestination?.contentKey?.value
                if (taskId != null) {
                    TaskDetailScreen(
                        isSinglePane = false,
                        taskId = taskId,
                        onUpPress = {
                            coroutineScope.launch { navigator.navigateBack() }
                        },
                    )
                } else {
                    DefaultIconTextContent(
                        icon = Icons.Outlined.CheckCircle,
                        iconContentDescription = stringResource(Res.string.task_list_cd_error),
                        header = stringResource(Res.string.task_detail_pane_title),
                    )
                }
            }
        },
    )
}

@Suppress("LongMethod", "LongParameterList")
@Composable
internal fun TaskListScaffold(
    taskViewState: TaskListViewState,
    longTermTask: TaskWithCategory?,
    categoryViewState: CategoryState,
    onFabClick: () -> Unit,
    onTaskCheckedChange: (TaskWithCategory) -> Unit,
    onItemClick: (Long) -> Unit,
    currentCategory: CategoryId?,
    onCategoryChange: (CategoryId?) -> Unit,
    refreshKey: Int,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarTitle = stringResource(Res.string.task_snackbar_message_complete)
    val snackbarButton = stringResource(Res.string.task_snackbar_button_undo)

    val onShowSnackbar: (TaskWithCategory) -> Unit = { taskWithCategory ->
        coroutineScope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = snackbarTitle,
                actionLabel = snackbarButton,
                duration = SnackbarDuration.Short,
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    // Do nothing
                }

                SnackbarResult.ActionPerformed -> {
                    onTaskCheckedChange(taskWithCategory)
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TaskFilter(
                categoryState = categoryViewState,
                currentCategory = currentCategory,
                onCategoryChange = onCategoryChange,
            )
        },
        floatingActionButton = {
            AddFloatingButton(
                contentDescription = stringResource(Res.string.task_cd_add_task),
                onClick = onFabClick,
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        Crossfade(
            targetState = taskViewState,
            modifier = Modifier.padding(paddingValues),
        ) { state ->
            when (state) {
                TaskListViewState.Loading -> {
                    AlkaaLoadingContent()
                }

                is TaskListViewState.Error -> {
                    TaskListError()
                }

                is TaskListViewState.Loaded -> {
                    TaskListContent(
                        taskList = state.items,
                        longTermTask = longTermTask,
                        onItemClick = onItemClick,
                        refreshKey = refreshKey,
                        onCheckedChange = { taskWithCategory ->
                            onTaskCheckedChange(taskWithCategory)
                            onShowSnackbar(taskWithCategory)
                        },
                    )
                }

                TaskListViewState.Empty -> {
                    TaskListEmpty()
                }
            }
        }
    }
}

@Composable
private fun TaskFilter(
    categoryState: CategoryState,
    currentCategory: CategoryId?,
    onCategoryChange: (CategoryId?) -> Unit,
) {
    CategorySelection(
        state = categoryState,
        currentCategory = currentCategory?.value,
        onCategoryChange = onCategoryChange,
        contentPadding = PaddingValues(horizontal = 16.dp),
    )
}

@Composable
private fun TaskListContent(
    taskList: ImmutableList<TaskWithCategory>,
    longTermTask: TaskWithCategory?,
    onItemClick: (Long) -> Unit,
    onCheckedChange: (TaskWithCategory) -> Unit,
    refreshKey: Int,
) {
    val displayedTasks = taskList.take(MaxTaskCount)
    val listState = rememberLazyListState()

    androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = 48.dp),
            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        ) {
            if (longTermTask != null) {
                item {
                    LongTermTaskSection(
                        task = longTermTask,
                        onItemClick = onItemClick,
                        onCheckedChange = onCheckedChange,
                    )
                }
            }
            items(
                items = displayedTasks,
                key = { taskWithCategory -> taskWithCategory.task.id },
                itemContent = { task ->
                    TaskItem(
                        task = task,
                        onItemClick = onItemClick,
                        onCheckedChange = onCheckedChange,
                    )
                },
            )
        }
        TaskListScrollbar(
            listState = listState,
            modifier = Modifier.align(Alignment.CenterEnd).padding(end = 2.dp),
        )
    }
}

@Composable
private fun TaskListEmpty() {
    DefaultIconTextContent(
        icon = Icons.Outlined.ThumbUp,
        iconContentDescription = stringResource(Res.string.task_list_cd_empty_list),
        header = stringResource(Res.string.task_list_header_empty),
    )
}

@Composable
private fun TaskListError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = stringResource(Res.string.task_list_cd_error),
        header = stringResource(Res.string.task_list_header_error),
    )
}

@Composable
private fun LongTermTaskSection(
    task: TaskWithCategory,
    onItemClick: (Long) -> Unit,
    onCheckedChange: (TaskWithCategory) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
    ) {
        Text(
            text = stringResource(Res.string.task_list_header_long_term),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
        TaskItem(
            task = task,
            onItemClick = onItemClick,
            onCheckedChange = onCheckedChange,
        )
    }
}

/**
 * Returns a key that changes from time to time, allowing the UI to refresh. This is useful to
 * update the relative time string in the cards.
 */
@Composable
private fun rememberRefreshKey(): State<Int> {
    var refreshKey by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(ComposableRefreshTime)
            refreshKey += 1
        }
    }
    return rememberUpdatedState(refreshKey)
}

@Composable
expect fun TaskListScrollbar(
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier,
)

/**
 * One minute in milliseconds.
 */
private const val ComposableRefreshTime = 60_000L

/**
 * Maximum number of ordinary tasks to display.
 */
private const val MaxTaskCount = 100
