package com.org.basshead.feature.dashboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.feature.dashboard.model.FestivalSuggestionState
import com.org.basshead.feature.dashboard.presentation.DashBoardActions
import com.org.basshead.feature.dashboard.presentation.DashBoardViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashBoardViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val isLoadingMore by viewModel.isLoadingMore.collectAsStateWithLifecycle()
    val hasMore by viewModel.hasMore.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var showError by remember { mutableStateOf(true) }

    // Track pagination triggers to avoid unnecessary calls
    var lastTriggeredIndex by remember { mutableIntStateOf(-1) }

    // Helper to extract the list safely from UiState.Content
    fun extractFestivalList(uiState: UiState<*>): List<FestivalSuggestionState> =
        (uiState as? UiState.Content<*>)?.data as? List<FestivalSuggestionState> ?: emptyList()

    // Optimized pagination trigger with debouncing
    LaunchedEffect(listState, hasMore) {
        snapshotFlow {
            listState.layoutInfo.let { layoutInfo ->
                Triple(
                    layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1,
                    layoutInfo.totalItemsCount,
                    layoutInfo.visibleItemsInfo.size,
                )
            }
        }
            .distinctUntilChanged() // Only emit when values actually change
            .collect { (lastVisibleIndex, totalItems, visibleCount) ->
                val festivalList = extractFestivalList(state.value)

                // Only trigger pagination if:
                // 1. We have items to show
                // 2. We're near the end (within 2 items)
                // 3. We haven't already triggered for this position
                // 4. We're not already loading more
                // 5. There might be more data
                // 6. We're not in an error state
                if (festivalList.isNotEmpty() &&
                    lastVisibleIndex >= totalItems - 1 &&
                    lastVisibleIndex > lastTriggeredIndex &&
                    !isLoadingMore &&
                    hasMore &&
                    lastVisibleIndex >= 0 &&
                    state.value !is UiState.Error
                ) {
                    lastTriggeredIndex = lastVisibleIndex
                    viewModel.onAction(DashBoardActions.LoadMore)
                }
            }
    }

    // Reset trigger when data changes significantly (e.g., refresh)
    LaunchedEffect(state.value) {
        val currentList = extractFestivalList(state.value)
        if (currentList.isEmpty()) {
            lastTriggeredIndex = -1
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val festivalList = extractFestivalList(currentState)

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                ) {
                    items(
                        items = festivalList,
                        key = { it.id },
                    ) { festival ->
                        FestivalItem(
                            festival = festival,
                            modifier = Modifier.animateItem(),
                        )
                    }

                    // Show loading indicator when loading more (but not initial load)
                    if (isLoadingMore && festivalList.isNotEmpty()) {
                        item(key = "loading_more") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                )
                            }
                        }
                    }

                    // Show "No more items" indicator when pagination ends
                    if (!isLoadingMore && festivalList.isNotEmpty() && !hasMore) {
                        item(key = "end_of_list") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = "No more festivals to show",
                                    style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface,
                                )
                            }
                        }
                    }
                }
                if (currentState.isLoadingUi && festivalList.isEmpty()) {
                    LoadingScreen()
                }
            }
        }

        is UiState.Error -> {
            if (showError) {
                ErrorScreen(
                    errorMessage = currentState.message.asString(),
                    onDismiss = { showError = false },
                    onRetry = {
                        showError = false
                        viewModel.onAction(DashBoardActions.Refresh)
                    },
                )
            }
        }

        is UiState.Navigate -> {
            when (currentState.route) {
                is Route.InternalDirection -> navigate(
                    currentState.route.destination,
                    currentState.route.popUpTp,
                    currentState.route.inclusive,
                )
                is Route.Back -> navigate("back", null, null)
            }
        }
    }
}
