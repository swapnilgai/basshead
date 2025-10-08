package com.org.basshead.feature.search.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.org.basshead.design.organisms.BassheadSearchScreenLayout
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.feature.search.presentation.SearchActions

@Stable
data class SearchDisplayData(
    val shouldTriggerLoadMore: Boolean,
    val optimizedUiState: SearchUiState,
)

/**
 * Optimized SearchScreen using atomic design with organism pattern
 * Follows the same pattern as ProfileScreen with performance optimizations
 *
 * Performance improvements:
 * - Stable data classes to minimize recomposition
 * - Optimized load more detection with derivedStateOf
 * - Direct composition through organism layout
 * - Eliminated intermediate wrapper components
 */
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onAction: (SearchActions) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    // Optimized load more detection using derivedStateOf for better performance
    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            // Determine which load more action to trigger based on current state
            when {
                uiState.shouldShowSearchResults && uiState.hasMoreResults -> {
                    !uiState.isLoadingMore &&
                        totalItemsNumber > 0 &&
                        lastVisibleItemIndex >= (totalItemsNumber - 3)
                }
                uiState.shouldShowSuggestions && uiState.hasMoreSuggestions -> {
                    !uiState.isLoadingMore &&
                        totalItemsNumber > 0 &&
                        lastVisibleItemIndex >= (totalItemsNumber - 3)
                }
                else -> false
            }
        }
    }

    // Trigger appropriate load more action
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            when {
                uiState.shouldShowSearchResults -> onAction(SearchActions.LoadMoreResults)
                uiState.shouldShowSuggestions -> onAction(SearchActions.LoadMoreSuggestions)
            }
        }
    }

    // Remember stable display data to minimize recomposition
    val searchDisplayData = remember(uiState, shouldLoadMore) {
        SearchDisplayData(
            shouldTriggerLoadMore = shouldLoadMore,
            optimizedUiState = uiState,
        )
    }

    // Use atomic design organism for layout
    BassheadSearchScreenLayout(
        uiState = searchDisplayData.optimizedUiState,
        onAction = onAction,
        listState = listState,
        modifier = modifier,
    )
}
