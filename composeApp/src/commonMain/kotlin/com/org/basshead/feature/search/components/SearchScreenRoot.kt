package com.org.basshead.feature.search.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.feature.dashboard.components.ErrorScreen
import com.org.basshead.feature.search.model.SearchUiState
import com.org.basshead.feature.search.presentation.SearchActions
import com.org.basshead.feature.search.presentation.SearchViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    // Remember callback functions to avoid recomposition
    val onSearchQueryChange = remember<(String) -> Unit> {
        {
                query ->
            viewModel.onAction(SearchActions.SearchQueryChanged(query))
        }
    }

    val onSearch = remember<() -> Unit> {
        {
            viewModel.onAction(SearchActions.Search)
        }
    }

    val onJoinFestival = remember<(String) -> Unit> {
        {
                festivalId ->
            // TODO: Implement join festival logic
        }
    }

    val onViewLeaderboard = remember<(String) -> Unit> {
        { festivalId ->
            navigate("leaderboard/$festivalId", null, null)
        }
    }
    
    val onLoadMore = remember<() -> Unit> {
        {
            viewModel.onAction(SearchActions.LoadMore)
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val searchUiState = currentState.data as SearchUiState
            SearchScreen(
                searchQuery = searchUiState.searchQuery,
                suggestionFestivals = searchUiState.suggestionFestivals,
                isSearching = searchUiState.isSearching,
                hasMoreSuggestions = searchUiState.hasMoreSuggestions,
                onSearchQueryChange = onSearchQueryChange,
                onSearch = onSearch,
                onLoadMore = onLoadMore,
                onJoinFestival = onJoinFestival,
                onViewLeaderboard = onViewLeaderboard,
                modifier = modifier,
            )

            if (currentState.isLoadingUi) {
                LoadingScreen()
            }
        }

        is UiState.Error -> {
            if (showError) {
                ErrorScreen(
                    errorMessage = currentState.message.asString(),
                    onDismiss = { showError = false },
                    onRetry = {
                        showError = false
                        viewModel.onAction(SearchActions.Refresh)
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
