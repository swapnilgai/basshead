package com.org.basshead.feature.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.feature.dashboard.components.ErrorScreen
import com.org.basshead.feature.profile.model.ProfileUiState
import com.org.basshead.feature.profile.presentation.ProfileActions
import com.org.basshead.feature.profile.presentation.ProfileViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreenRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    // Remember callback functions to avoid recomposition
    val onLogout = remember<() -> Unit> {
        {
            viewModel.onAction(ProfileActions.Logout)
        }
    }

    val onViewLeaderboard = remember<(String) -> Unit> {
        {
                festivalId ->
            viewModel.onAction(ProfileActions.ViewLeaderboard(festivalId))
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val profileUiState = currentState.data as ProfileUiState
            ProfileScreen(
                profile = profileUiState.profile,
                userFestivals = profileUiState.userFestivals,
                dailyHeadbangs = profileUiState.dailyHeadbangs,
                totalHeadbangs = profileUiState.totalHeadbangs,
                onLogout = onLogout,
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
                        viewModel.onAction(ProfileActions.Refresh)
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
