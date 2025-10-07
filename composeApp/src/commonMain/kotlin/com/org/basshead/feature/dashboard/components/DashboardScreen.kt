package com.org.basshead.feature.dashboard.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.design.organisms.BassheadDashboardScreenLayout
import com.org.basshead.feature.dashboard.model.DashBoardUiState
import com.org.basshead.feature.dashboard.presentation.DashBoardActions
import com.org.basshead.feature.dashboard.presentation.DashBoardViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashBoardViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    // Remember callback functions to avoid recomposition - following MVI pattern
    val onFestivalClick = remember<(String) -> Unit> {
        { festivalId ->
            viewModel.onAction(DashBoardActions.OnFestivalClicked(festivalId))
        }
    }

    val onJoinFestival = remember<(String) -> Unit> {
        { festivalId ->
            viewModel.onAction(DashBoardActions.JoinFestival(festivalId))
        }
    }

    val onViewLeaderboard = remember<(String) -> Unit> {
        { festivalId ->
            viewModel.onAction(DashBoardActions.ViewLeaderboard(festivalId))
        }
    }

    val onRefresh = remember<() -> Unit> {
        {
            viewModel.onAction(DashBoardActions.Refresh)
        }
    }

    val onSyncDevice = remember<() -> Unit> {
        {
            viewModel.onAction(DashBoardActions.SyncDevice)
        }
    }

    val onOpenSettings = remember<() -> Unit> {
        {
            viewModel.onAction(DashBoardActions.OpenSettings)
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val dashBoardUiState = currentState.data as DashBoardUiState
            DashboardScreen(
                dashBoardUiState = dashBoardUiState,
                onFestivalClick = onFestivalClick,
                onJoinFestival = onJoinFestival,
                onViewLeaderboard = onViewLeaderboard,
                onRefresh = onRefresh,
                onSyncDevice = onSyncDevice,
                onOpenSettings = onOpenSettings,
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
                        viewModel.onAction(DashBoardActions.Refresh)
                    },
                )
            }
        }

        is UiState.Navigate -> {
            // Handle navigation events - same pattern as ProfileScreenRoot
            when (val route = currentState.route) {
                is Route.InternalDirection -> {
                    navigate(
                        route.destination,
                        route.popUpTp,
                        route.inclusive,
                    )
                }
                is Route.Back -> {
                    navigate("back", null, null)
                }
            }
        }
    }
}

@Composable
fun DashboardScreen(
    dashBoardUiState: DashBoardUiState,
    onFestivalClick: (String) -> Unit,
    onJoinFestival: (String) -> Unit,
    onViewLeaderboard: (String) -> Unit,
    onRefresh: () -> Unit,
    onSyncDevice: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Use computed property from DashBoardUiState
    val totalHeadbangs = remember(dashBoardUiState.totalHeadbangs) {
        dashBoardUiState.totalHeadbangs.toInt()
    }

    val featuredFestival = remember(dashBoardUiState.joinedFestivals, dashBoardUiState.suggestionFestivals) {
        when {
            dashBoardUiState.joinedFestivals.isNotEmpty() -> {
                dashBoardUiState.joinedFestivals.first().copy(userJoined = true)
            }
            dashBoardUiState.suggestionFestivals.isNotEmpty() -> {
                dashBoardUiState.suggestionFestivals.first().copy(userJoined = false)
            }
            else -> null
        }
    }

    val showNoFestivalsMessage = remember(dashBoardUiState.joinedFestivals, dashBoardUiState.suggestionFestivals) {
        dashBoardUiState.joinedFestivals.isEmpty() && dashBoardUiState.suggestionFestivals.isEmpty()
    }

    // Use the atomic design organism layout
    BassheadDashboardScreenLayout(
        profile = dashBoardUiState.profile,
        totalHeadbangs = totalHeadbangs,
        isDeviceConnected = dashBoardUiState.isDeviceConnected,
        isSyncing = dashBoardUiState.isSyncing,
        featuredFestival = featuredFestival,
        showNoFestivalsMessage = showNoFestivalsMessage,
        onFestivalClick = onFestivalClick,
        onJoinFestival = onJoinFestival,
        onViewLeaderboard = onViewLeaderboard,
        onSyncDevice = onSyncDevice,
        onOpenSettings = onOpenSettings,
        modifier = modifier,
    )
}
