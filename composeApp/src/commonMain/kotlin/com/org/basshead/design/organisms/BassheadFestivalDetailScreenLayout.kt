package com.org.basshead.design.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadDisplayLarge
import com.org.basshead.design.atoms.BassheadHeadlineLarge
import com.org.basshead.design.atoms.BassheadStatusBadgeAtom
import com.org.basshead.design.molecules.BassheadFestivalActionsCardMolecule
import com.org.basshead.design.molecules.BassheadFestivalDateTimeCardMolecule
import com.org.basshead.design.molecules.BassheadFestivalDescriptionCardMolecule
import com.org.basshead.design.molecules.BassheadFestivalErrorStateMolecule
import com.org.basshead.design.molecules.BassheadFestivalQuickInfoRowMolecule
import com.org.basshead.design.molecules.BassheadFestivalStatsCardMolecule
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.festivaldetail.model.FestivalDetailUiState
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailActions
import kotlin.math.min

/**
 * Enhanced Festival Detail Screen Layout Organism
 * Modern UI/UX following Airbnb/Spotify design patterns with Material Design 3
 *
 * Key Design Improvements:
 * - Immersive hero section with better imagery
 * - Improved color contrast and accessibility
 * - Better spacing and visual hierarchy
 * - Modern card designs with proper elevation
 * - Smooth animations and transitions
 * - Proper theme integration
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BassheadFestivalDetailScreenLayout(
    uiState: FestivalDetailUiState,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val festival = uiState.festival
    val scrollState = rememberLazyListState()

    // Enhanced parallax effect with smoother animation
    val parallaxOffset by remember {
        derivedStateOf {
            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset

            if (firstVisibleItemIndex == 0) {
                min(firstVisibleItemScrollOffset * 0.3f, 150f) // Reduced for smoother effect
            } else {
                150f
            }
        }
    }

    // Header visibility with smooth transition
    val headerAlpha by animateFloatAsState(
        targetValue = if (scrollState.firstVisibleItemScrollOffset > 200) 1f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "headerAlpha"
    )

    // FAB visibility based on scroll
    val fabVisible by remember {
        derivedStateOf {
            scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > 400
        }
    }

    // Pull to refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { onAction(FestivalDetailActions.Refresh) },
    )

    // Error handling
    if (festival == null && !uiState.isRefreshing) {
        BassheadFestivalErrorStateMolecule(
            error = uiState.joinError ?: "Festival not found",
            onRetry = { onAction(FestivalDetailActions.Refresh) },
            onBack = { onAction(FestivalDetailActions.NavigateBack) },
            modifier = modifier.fillMaxSize(),
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(BassheadTheme.colors.background), // Use theme background
    ) {
        festival?.let {
            // Main content with improved spacing
            LazyColumn(
                state = scrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp), // More space for FAB
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                // Enhanced Hero Section
                item(key = "hero") {
                    EnhancedHeroSection(
                        festival = festival,
                        parallaxOffset = parallaxOffset,
                        onBack = { onAction(FestivalDetailActions.NavigateBack) },
                        onShare = { /* TODO: Implement share */ },
                        onFavorite = { /* TODO: Implement favorite */ },
                    )
                }

                // Improved Content sections
                item(key = "content") {
                    EnhancedContentSection(
                        festival = festival,
                        isJoining = uiState.isJoining,
                        onAction = onAction,
                    )
                }
            }

            // Floating header (appears on scroll)
            AnimatedVisibility(
                visible = headerAlpha > 0.5f,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .zIndex(3f),
            ) {
                FloatingHeader(
                    festival = festival,
                    onBack = { onAction(FestivalDetailActions.NavigateBack) },
                    alpha = headerAlpha,
                )
            }

            // Enhanced Floating action button
            AnimatedVisibility(
                visible = fabVisible,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(400, easing = FastOutSlowInEasing)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(BassheadTheme.spacing.large)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .zIndex(1f),
            ) {
                EnhancedFloatingActionButton(
                    festival = festival,
                    isJoining = uiState.isJoining,
                    onAction = onAction,
                )
            }
        }

        // Enhanced Pull to refresh indicator
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars)
                .zIndex(2f),
            backgroundColor = BassheadTheme.colors.surface,
            contentColor = BassheadTheme.colors.primary,
        )
    }
}

/**
 * Enhanced hero section with improved imagery and actions
 */
@Composable
private fun EnhancedHeroSection(
    festival: FestivalItemState,
    parallaxOffset: Float,
    onBack: () -> Unit,
    onShare: () -> Unit,
    onFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp),
    ) {
        // Background Image with parallax effect
        AsyncImage(
            model = festival.imageUrl,
            contentDescription = "Festival Image",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationY = -parallaxOffset
                },
            contentScale = ContentScale.Crop,
        )

        // Gradient overlay
        val gradientOverlay = remember {
            Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.3f),
                    Color.Transparent,
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.8f),
                ),
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientOverlay),
        )

        // Status bar spacing
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars),
        )

        // Back button
        Surface(
            modifier = Modifier
                .padding(BassheadTheme.spacing.large)
                .size(48.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f),
            shadowElevation = 2.dp,
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                )
            }
        }

        // Share button
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(BassheadTheme.spacing.large)
                .size(48.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f),
            shadowElevation = 2.dp,
        ) {
            IconButton(
                onClick = onShare,
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                )
            }
        }

        // Favorite button
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(BassheadTheme.spacing.large)
                .size(48.dp)
                .alpha(0.8f), // Slightly transparent
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f),
            shadowElevation = 2.dp,
        ) {
            IconButton(
                onClick = onFavorite,
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = Color.White,
                )
            }
        }

        // Festival info overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(BassheadTheme.spacing.large)
                .fillMaxWidth(),
        ) {
            // Status badge
            if (festival.status.isNotEmpty()) {
                BassheadStatusBadgeAtom(
                    status = festival.status,
                    modifier = Modifier.padding(bottom = BassheadTheme.spacing.medium),
                )
            }

            BassheadHeadlineLarge(
                text = festival.name,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(BassheadTheme.spacing.small))
                BassheadBodyLarge(
                    text = festival.location,
                    color = Color.White.copy(alpha = 0.9f),
                )
            }
        }
    }
}

/**
 * Floating header that appears on scroll
 */
@Composable
private fun FloatingHeader(
    festival: FestivalItemState,
    onBack: () -> Unit,
    alpha: Float,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(BassheadTheme.spacing.large)
            .alpha(alpha),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Back button
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = BassheadTheme.colors.onBackground,
            )
        }

        // Festival title
        BassheadDisplayLarge(
            text = festival.name,
            color = BassheadTheme.colors.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // Empty space for alignment
        Spacer(modifier = Modifier.size(48.dp))
    }
}

/**
 * Content section with all the cards
 */
@Composable
private fun EnhancedContentSection(
    festival: FestivalItemState,
    isJoining: Boolean,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = BassheadTheme.spacing.medium), // Reduced from large
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium), // Reduced from large
    ) {
        // Add some top spacing from hero section
        Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

        // Quick Info Row
        BassheadFestivalQuickInfoRowMolecule(
            festival = festival,
        )

        // Date & Time Information
        festival.dateString?.takeIf { it.isNotBlank() }?.let { dateString: String ->
            BassheadFestivalDateTimeCardMolecule(
                dateString = dateString,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Description
        festival.description?.takeIf { it.isNotBlank() }?.let { description: String ->
            BassheadFestivalDescriptionCardMolecule(
                description = description,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Stats (if joined)
        if (festival.userJoined) {
            BassheadFestivalStatsCardMolecule(
                festival = festival,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // Actions Section
        BassheadFestivalActionsCardMolecule(
            festival = festival,
            isJoining = isJoining,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )

        // Bottom spacing for FAB
        Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))
    }
}

/**
 * Enhanced floating action button with improved design
 */
@Composable
private fun EnhancedFloatingActionButton(
    festival: FestivalItemState,
    isJoining: Boolean,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = {
            if (festival.userJoined) {
                onAction(FestivalDetailActions.ViewLeaderboard)
            } else {
                onAction(FestivalDetailActions.JoinFestival)
            }
        },
        modifier = modifier,
        containerColor = BassheadTheme.colors.primary,
        contentColor = BassheadTheme.colors.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = BassheadTheme.elevation.level3,
        ),
        shape = RoundedCornerShape(BassheadTheme.spacing.large),
    ) {
        Icon(
            imageVector = if (festival.userJoined) Icons.Default.Star else Icons.Default.MusicNote,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
        )
        Spacer(modifier = Modifier.width(BassheadTheme.spacing.small))
        BassheadBodyLarge(
            text = if (festival.userJoined) "Leaderboard" else if (isJoining) "Joining..." else "Join Now",
            color = BassheadTheme.colors.onPrimary,
        )
    }
}
