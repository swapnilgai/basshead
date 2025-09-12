package com.org.basshead.feature.festivaldetail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.org.basshead.design.organisms.BassheadFestivalDetailScreenLayout
import com.org.basshead.feature.festivaldetail.model.FestivalDetailUiState
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailActions

/**
 * Festival Detail Screen following atomic design pattern
 * Uses BassheadTheme design system for consistent UI/UX
 */

/**
 * Festival Detail Screen component
 *
 * This screen displays detailed information about a festival including:
 * - Hero section with festival image and basic info
 * - Quick stats row showing participants, status, and user rank
 * - Event schedule information
 * - Festival description
 * - User stats (if joined)
 * - Action buttons for joining or viewing leaderboard
 *
 * Features:
 * - Parallax scrolling hero section
 * - Pull-to-refresh functionality
 * - Floating action button
 * - Animated visibility for stats
 * - Error handling with retry functionality
 * - Loading states for actions
 */
@Composable
fun FestivalDetailScreen(
    uiState: FestivalDetailUiState,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Use the organism layout component directly
    BassheadFestivalDetailScreenLayout(
        uiState = uiState,
        onAction = onAction,
        modifier = modifier,
    )
}
