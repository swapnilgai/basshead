package com.org.basshead.feature.festivaldetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.festival_participants
import basshead.composeapp.generated.resources.join
import basshead.composeapp.generated.resources.joining
import basshead.composeapp.generated.resources.leaderboard
import basshead.composeapp.generated.resources.total_headbangs
import basshead.composeapp.generated.resources.your_rank
import coil3.compose.AsyncImage
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.festivaldetail.model.FestivalDetailUiState
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailActions
import com.org.basshead.utils.core.LightOrange
import com.org.basshead.utils.core.PrimaryOrange
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FestivalDetailScreen(
    uiState: FestivalDetailUiState,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    val festival = uiState.festival
    val scrollState = rememberScrollState()
    val hapticFeedback = LocalHapticFeedback.current
    
    // Pull to refresh state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { onAction(FestivalDetailActions.Refresh) }
    )

    // Enhanced error handling
    if (festival == null && !uiState.isRefreshing) {
        ErrorStateScreen(
            error = uiState.joinError ?: "Festival not found",
            onRetry = { onAction(FestivalDetailActions.Refresh) },
            onBack = { onAction(FestivalDetailActions.NavigateBack) }
        )
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        festival?.let {
            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Hero Section with Parallax Effect
                HeroSection(
                    festival = festival,
                    scrollOffset = scrollState.value,
                    onBack = { onAction(FestivalDetailActions.NavigateBack) }
                )

                // Content Section with better spacing and visual hierarchy
                ContentSection(
                    festival = festival,
                    isJoining = uiState.isJoining,
                    onAction = onAction,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                // Bottom spacing for navigation
                Spacer(modifier = Modifier.height(100.dp))
            }

            // Floating Action Button for primary action
            FloatingActionButton(
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (festival.userJoined) {
                        onAction(FestivalDetailActions.ViewLeaderboard)
                    } else {
                        onAction(FestivalDetailActions.JoinFestival)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .semantics {
                        contentDescription = if (festival.userJoined) {
                            "View leaderboard for ${festival.name}"
                        } else {
                            "Join ${festival.name}"
                        }
                    },
                backgroundColor = PrimaryOrange,
                contentColor = Color.White
            ) {
                if (uiState.isJoining) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = if (festival.userJoined) Icons.Default.Star else Icons.Default.MusicNote,
                        contentDescription = null
                    )
                }
            }
        }

        // Pull to refresh indicator
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.statusBars),
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = PrimaryOrange
        )
    }
}

@Composable
private fun HeroSection(
    festival: FestivalItemState,
    scrollOffset: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val parallaxOffset = (scrollOffset * 0.5f).coerceAtMost(200f)
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // Background Image with Parallax
        AsyncImage(
            model = festival.imageUrl,
            contentDescription = "Festival Image",
            modifier = Modifier
                .fillMaxSize()
                .padding(top = -parallaxOffset.dp),
            contentScale = ContentScale.Crop,
        )
        
        // Gradient overlays for better readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Status Bar Safe Area
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
        )

        // Back Button with circular background
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f),
            elevation = 2.dp
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        // Share Button
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(48.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f),
            elevation = 2.dp
        ) {
            IconButton(
                onClick = { /* TODO: Implement share */ },
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White
                )
            }
        }

        // Festival Info Overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Festival Status Badge
            if (festival.status.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (festival.status.lowercase()) {
                        "ongoing" -> Color.Green.copy(alpha = 0.9f)
                        "upcoming" -> PrimaryOrange.copy(alpha = 0.9f)
                        else -> Color.Gray.copy(alpha = 0.9f)
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = festival.status.uppercase(),
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            Text(
                text = festival.name,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = festival.location,
                    style = MaterialTheme.typography.body1,
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun ContentSection(
    festival: FestivalItemState,
    isJoining: Boolean,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(24.dp))

        // Quick Info Cards Row
        QuickInfoRow(festival = festival)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Date & Time Information
        festival.dateString?.let { dateString ->
            InfoCard(
                title = "Event Schedule",
                icon = Icons.Default.CalendarToday,
                content = {
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Time",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "All Day Event",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Description with better typography
        festival.description?.let { description ->
            if (description.isNotBlank()) {
                InfoCard(
                    title = "About This Festival",
                    content = {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.body1,
                            lineHeight = 24.sp,
                            color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Enhanced Stats Section
        StatsSection(festival = festival)
        
        Spacer(modifier = Modifier.height(24.dp))

        // Secondary Actions
        SecondaryActionsSection(
            festival = festival,
            isJoining = isJoining,
            onAction = onAction
        )
    }
}

@Composable
private fun ErrorStateScreen(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Oops!",
                style = MaterialTheme.typography.h5,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = error,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("Go Back")
                }
                
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryOrange)
                ) {
                    Text("Retry", color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun QuickInfoRow(
    festival: FestivalItemState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Participants Quick Card
        QuickInfoCard(
            icon = Icons.Default.Person,
            value = festival.totalParticipants.toString(),
            label = "Participants",
            modifier = Modifier.weight(1f)
        )
        
        // Status Quick Card
        QuickInfoCard(
            icon = Icons.Default.AccessTime,
            value = festival.status.replaceFirstChar { it.uppercase() },
            label = "Status",
            modifier = Modifier.weight(1f),
            valueColor = when (festival.status.lowercase()) {
                "ongoing" -> Color.Green
                "upcoming" -> PrimaryOrange
                else -> MaterialTheme.colors.onSurface
            }
        )
        
        // User Status (if joined)
        if (festival.userJoined) {
            festival.userRank?.let { rank ->
                QuickInfoCard(
                    icon = Icons.Default.Star,
                    value = "#$rank",
                    label = "Your Rank",
                    modifier = Modifier.weight(1f),
                    valueColor = PrimaryOrange
                )
            }
        }
    }
}

@Composable
private fun QuickInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colors.onSurface,
) {
    Card(
        modifier = modifier,
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryOrange,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
            
            Text(
                text = label,
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: @Composable () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = PrimaryOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun StatsSection(
    festival: FestivalItemState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = festival.userJoined,
        enter = slideInVertically() + fadeIn(),
        exit = fadeOut()
    ) {
        InfoCard(
            title = "Your Festival Stats",
            icon = Icons.Default.Star,
            content = {
                Column {
                    festival.totalHeadbangs?.let { totalHeadbangs ->
                        StatRow(
                            label = stringResource(Res.string.total_headbangs),
                            value = totalHeadbangs.toString(),
                            icon = Icons.Default.MusicNote
                        )
                        
                        if (festival.userRank != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f))
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    
                    festival.userRank?.let { userRank ->
                        StatRow(
                            label = stringResource(Res.string.your_rank),
                            value = "#$userRank",
                            icon = Icons.Default.Star
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun StatRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryOrange,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.8f)
            )
        }
        
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            color = PrimaryOrange
        )
    }
}

@Composable
private fun SecondaryActionsSection(
    festival: FestivalItemState,
    isJoining: Boolean,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Actions",
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold
            )
            
            if (!festival.userJoined) {
                OutlinedButton(
                    onClick = { onAction(FestivalDetailActions.JoinFestival) },
                    enabled = !isJoining,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryOrange
                    )
                ) {
                    if (isJoining) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = PrimaryOrange,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(Res.string.joining))
                    } else {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(Res.string.join))
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { onAction(FestivalDetailActions.ViewLeaderboard) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = PrimaryOrange
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(Res.string.leaderboard))
                }
            }
        }
    }
}
