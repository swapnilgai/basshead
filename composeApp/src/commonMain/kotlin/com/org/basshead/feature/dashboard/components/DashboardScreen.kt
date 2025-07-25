package com.org.basshead.feature.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.featured_festival
import basshead.composeapp.generated.resources.no_festivals_available
import basshead.composeapp.generated.resources.welcome_default_user
import basshead.composeapp.generated.resources.welcome_user
import basshead.composeapp.generated.resources.your_current_festival
import com.org.basshead.feature.dashboard.model.DashBoardUiState
import com.org.basshead.feature.dashboard.presentation.DashBoardActions
import com.org.basshead.feature.dashboard.presentation.DashBoardViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot(
    viewModel: DashBoardViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    // Remember callback functions to avoid recomposition
    val onJoinFestival = remember<(String) -> Unit> {
        {
                festivalId ->
            viewModel.onAction(DashBoardActions.JoinFestival(festivalId))
        }
    }

    val onViewLeaderboard = remember<(String) -> Unit> {
        {
                festivalId ->
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

@Composable
fun DashboardScreen(
    dashBoardUiState: DashBoardUiState,
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Welcome message
        item(key = "welcome_header") {
            Text(
                text = dashBoardUiState.profile?.name?.let { name ->
                    stringResource(Res.string.welcome_user, name)
                } ?: stringResource(Res.string.welcome_default_user),
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
            )
        }

        // Device sync card
        item(key = "device_sync_card") {
            DeviceSyncCard(
                totalHeadbangs = totalHeadbangs,
                isDeviceConnected = dashBoardUiState.isDeviceConnected,
                isSyncing = dashBoardUiState.isSyncing,
                onSyncClick = onSyncDevice,
                onSettingsClick = onOpenSettings,
            )
        }

        // Featured Festival (user joined or first suggestion)
        featuredFestival?.let { festival ->
            item(key = "featured_festival_${festival.id}") {
                Column {
                    Text(
                        text = if (festival.userJoined) {
                            stringResource(Res.string.your_current_festival)
                        } else {
                            stringResource(Res.string.featured_festival)
                        },
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )

                    FestivalItem(
                        festival = festival,
                        onJoinFestival = onJoinFestival,
                        onViewLeaderboard = onViewLeaderboard,
                    )
                }
            }
        }

        // Show message if no festivals
        if (showNoFestivalsMessage) {
            item(key = "no_festivals_message") {
                Text(
                    text = stringResource(Res.string.no_festivals_available),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}
