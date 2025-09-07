package com.org.basshead.feature.avatar.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.org.basshead.feature.avatar.model.Avatar
import com.org.basshead.feature.avatar.model.AvatarSelectionUiState
import com.org.basshead.feature.avatar.presentation.AvatarSelectionActions
import com.org.basshead.feature.avatar.presentation.AvatarSelectionViewModel
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.absoluteValue

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
                ErrorScreen(
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

@OptIn(ExperimentalMaterial3Api::class)
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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Avatar",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Button(
                    onClick = onSaveAvatar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !avatarUiState.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6B46C1),
                        disabledContainerColor = Color(0xFF6B46C1).copy(alpha = 0.5f),
                    ),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    if (avatarUiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { paddingValues ->
        if (avatarUiState.avatars.isNotEmpty()) {
            // ViewPager-style horizontal pager with zoom transformation
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 64.dp),
                pageSpacing = 16.dp,
                key = { index -> avatarUiState.avatars[index].url },
            ) { page ->
                // Calculate page offset for zoom transformation
                val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                // Apply zoom-in transformation similar to ViewPager2 PageTransformer
                val scale = lerp(
                    start = 0.8f,
                    stop = 1.0f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                )

                val alpha = lerp(
                    start = 0.5f,
                    stop = 1.0f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                )

                AvatarPagerCard(
                    avatar = avatarUiState.avatars[page],
                    isSelected = page == pagerState.currentPage,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                )
            }
        }
    }
}

@Composable
fun AvatarPagerCard(
    avatar: Avatar,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    // Large card size for ViewPager display
    val cardSize = 320.dp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        // Avatar image container - ensuring perfect circle with proper clipping
        AsyncImage(
            model = avatar.url,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(if (isSelected) cardSize else cardSize - 16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun ErrorScreen(
    errorMessage: String,
    onDismiss: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.error,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(onClick = onDismiss) {
                Text("Dismiss")
            }

            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
