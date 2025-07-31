package com.org.basshead.feature.search.components

import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel
import com.org.basshead.utils.ui.Route as BaseRoute

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    // Hoist the LazyListState here so it survives navigation
    val listState = rememberLazyListState()

    when (val currentState = state.value) {
        is UiState.Content -> {
            val searchUiState = currentState.data as SearchUiState
            SearchScreen(
                uiState = searchUiState,
                onAction = viewModel::onAction,
                listState = listState,
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
            when (val route = currentState.route) {
                is BaseRoute.Back -> {
                    // Handle back navigation if needed
                }
                is BaseRoute.InternalDirection -> {
                    navigate(route.destination, route.popUpTp, route.inclusive)
                }
            }
        }
    }
}
