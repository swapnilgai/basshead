package com.org.basshead.design.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadHeadlineSmall
import com.org.basshead.design.atoms.BassheadIconButton
import com.org.basshead.design.atoms.BassheadTitleLarge
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.avatar.model.Avatar
import kotlin.math.absoluteValue

/**
 * Atomic design organism for avatar selection screen
 * Follows the same patterns as BassheadProfileScreenLayout with proper theme integration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BassheadAvatarSelectionScreenLayout(
    avatars: List<Avatar>,
    pagerState: PagerState,
    isLoading: Boolean,
    onSaveAvatar: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    BassheadHeadlineSmall(
                        text = "Your Avatar",
                        color = BassheadTheme.colors.onSurface,
                    )
                },
                navigationIcon = {
                    BassheadIconButton(
                        onClick = onNavigateBack,
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BassheadTheme.colors.surface,
                ),
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BassheadTheme.spacing.large),
            ) {
                BassheadButton(
                    onClick = onSaveAvatar,
                    enabled = !isLoading,
                    isLoading = isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    BassheadTitleLarge(
                        text = "Save",
                        color = BassheadTheme.colors.onPrimary,
                    )
                }
            }
        },
        containerColor = BassheadTheme.colors.surface,
    ) { paddingValues ->
        if (avatars.isNotEmpty()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 64.dp),
                pageSpacing = BassheadTheme.spacing.large,
                key = { index -> avatars[index].url },
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

                BassheadAvatarPagerCard(
                    avatar = avatars[page],
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

/**
 * Atomic design molecule for avatar pager card
 * Follows the same atomic component patterns with proper theme integration
 */
@Composable
fun BassheadAvatarPagerCard(
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
                .background(BassheadTheme.colors.surface),
            contentScale = ContentScale.Fit,
        )
    }
}
