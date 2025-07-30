package com.org.basshead.feature.festivaldetail.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.feature.festivaldetail.model.FestivalDetailUiState
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailActions
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailViewModel
import com.org.basshead.utils.components.ErrorScreen
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FestivalDetailScreenRoot(
    festivalId: String,
    viewModel: FestivalDetailViewModel = koinViewModel { parametersOf(festivalId) },
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val festivalDetailUiState = currentState.data as FestivalDetailUiState
            FestivalDetailScreen(
                uiState = festivalDetailUiState,
                onAction = viewModel::onAction,
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
                    onDismiss = { 
                        showError = false
                        viewModel.onAction(FestivalDetailActions.Refresh)
                    },
                )
            }
        }

        is UiState.Navigate -> {
            when (val route = currentState.route) {
                is Route.Back -> {
                    // Handle back navigation
                    navigate("Dashboard", null, false)
                }
                is Route.InternalDirection -> {
                    navigate(route.destination, route.popUpTp, route.inclusive)
                }
            }
        }
    }
}
