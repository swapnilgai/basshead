package com.org.basshead.feature.avatar.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadHeadlineSmall
import com.org.basshead.design.atoms.BassheadOutlinedButton
import com.org.basshead.design.organisms.BassheadAvatarSelectionScreenLayout
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.avatar.model.AvatarSelectionUiState
import com.org.basshead.feature.avatar.presentation.AvatarSelectionActions
import com.org.basshead.feature.avatar.presentation.AvatarSelectionViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AvatarSelectionScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: AvatarSelectionViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit = { _, _, _ -> },
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    // Remember callback functions to avoid recomposition - following MVI pattern
    val onAvatarSelected = remember<(String) -> Unit> {
        {
                avatarUrl ->
            viewModel.onAction(AvatarSelectionActions.SelectAvatar(avatarUrl))
        }
    }

    val onSaveAvatar = remember<() -> Unit> {
        {
            viewModel.onAction(AvatarSelectionActions.SaveAvatar)
        }
    }

    val onNavigateBack = remember<() -> Unit> {
        {
            viewModel.onAction(AvatarSelectionActions.NavigateBack)
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            val avatarUiState = currentState.data as AvatarSelectionUiState
            AvatarSelectionScreen(
                avatarUiState = avatarUiState,
                onAvatarSelected = onAvatarSelected,
                onSaveAvatar = onSaveAvatar,
                onNavigateBack = onNavigateBack,
                modifier = modifier,
            )

            if (currentState.isLoadingUi || avatarUiState.isSaving) {
                LoadingScreen()
            }
        }

        is UiState.Error -> {
            if (showError) {
                BassheadErrorScreen(
                    errorMessage = currentState.message.asString(),
                    onDismiss = { showError = false },
                    onRetry = {
                        showError = false
                        viewModel.onAction(AvatarSelectionActions.LoadAvatars)
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
fun AvatarSelectionScreen(
    avatarUiState: AvatarSelectionUiState,
    onAvatarSelected: (String) -> Unit,
    onSaveAvatar: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Create pager state for ViewPager-like functionality
    val pagerState = rememberPagerState(pageCount = { avatarUiState.avatars.size })

    // Initialize with current user's avatar if available - only once on first load
    LaunchedEffect(pagerState, avatarUiState.avatars) {
        if (avatarUiState.avatars.isNotEmpty()) {
            val currentIndex = avatarUiState.avatars.indexOfFirst {
                it.url == avatarUiState.selectedAvatarUrl
            }
            if (currentIndex != -1) {
                pagerState.scrollToPage(currentIndex)
            }
        }
    }

    // Observe the current page and notify the view model when it changes.
    // derivedStateOf is used to ensure that onAvatarSelected is only called when the settled page changes.
    val settledPage by remember {
        derivedStateOf {
            pagerState.settledPage
        }
    }

    LaunchedEffect(settledPage) {
        if (avatarUiState.avatars.isNotEmpty() && settledPage < avatarUiState.avatars.size) {
            val selectedAvatarUrl = avatarUiState.avatars[settledPage].url
            if (selectedAvatarUrl != avatarUiState.selectedAvatarUrl) {
                onAvatarSelected(selectedAvatarUrl)
            }
        }
    }

    // Use the atomic design organism layout
    BassheadAvatarSelectionScreenLayout(
        avatars = avatarUiState.avatars,
        pagerState = pagerState,
        isLoading = avatarUiState.isSaving,
        onSaveAvatar = onSaveAvatar,
        onNavigateBack = onNavigateBack,
        modifier = modifier,
    )
}

/**
 * Error screen using atomic design components
 * Following the same patterns as profile package
 */
@Composable
fun BassheadErrorScreen(
    errorMessage: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(BassheadTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BassheadHeadlineSmall(
            text = "Error",
            color = BassheadTheme.colors.error,
        )

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

        BassheadBodyMedium(
            text = errorMessage,
            color = BassheadTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.large))

        Row(
            horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
        ) {
            BassheadOutlinedButton(onClick = onDismiss) {
                BassheadBodyMedium(text = "Dismiss")
            }

            BassheadButton(onClick = onRetry) {
                BassheadBodyMedium(text = "Retry")
            }
        }
    }
}
