package com.org.basshead.design.molecules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.festival_about_title
import basshead.composeapp.generated.resources.festival_join_button
import basshead.composeapp.generated.resources.festival_participants_label
import basshead.composeapp.generated.resources.festival_stats_icon_desc
import basshead.composeapp.generated.resources.festival_status_label
import basshead.composeapp.generated.resources.festival_total_headbangs_label
import basshead.composeapp.generated.resources.festival_view_leaderboard_button
import basshead.composeapp.generated.resources.festival_your_rank_label
import basshead.composeapp.generated.resources.festival_your_stats_title
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadErrorStateAtom
import com.org.basshead.design.atoms.BassheadInfoCardAtom
import com.org.basshead.design.atoms.BassheadLoadingButtonAtom
import com.org.basshead.design.atoms.BassheadStatRowAtom
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailActions
import org.jetbrains.compose.resources.stringResource

/**
 * Molecular components for Festival Detail Screen
 * Optimized with flattened hierarchy to reduce overdraw and improve performance
 */

/**
 * Quick info row molecule - Flattened with spacedBy arrangement
 */
@Composable
fun BassheadFestivalQuickInfoRowMolecule(
    festival: FestivalItemState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
    ) {
        // Participants
        BassheadInfoCardAtom(
            icon = Icons.Default.Person,
            value = "${festival.totalParticipants}",
            label = stringResource(Res.string.festival_participants_label),
            modifier = Modifier.weight(1f),
        )

        // Status - Using proper theme colors
        BassheadInfoCardAtom(
            icon = Icons.Default.Star,
            value = festival.status,
            label = stringResource(Res.string.festival_status_label),
            valueColor = when (festival.status.lowercase()) {
                "ongoing" -> BassheadTheme.colors.festivalActive
                "upcoming" -> BassheadTheme.colors.festivalUpcoming
                "completed" -> BassheadTheme.colors.festivalPast
                else -> BassheadTheme.colors.festivalInactive
            },
            modifier = Modifier.weight(1f),
        )

        // User Rank (if joined)
        if (festival.userJoined && festival.userRank != null) {
            BassheadInfoCardAtom(
                icon = Icons.Default.Star,
                value = "#${festival.userRank}",
                label = stringResource(Res.string.festival_your_rank_label),
                valueColor = BassheadTheme.colors.primary,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/**
 * Date time card molecule - Flattened hierarchy with spacedBy
 */
@Composable
fun BassheadFestivalDateTimeCardMolecule(
    dateString: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
        shape = RoundedCornerShape(BassheadTheme.spacing.medium),
    ) {
        Column(
            modifier = Modifier.padding(BassheadTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
        ) {
            // Header row with icon and title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "Date",
                    tint = BassheadTheme.colors.primary,
                    modifier = Modifier.size(24.dp),
                )
                BassheadTitleMedium(
                    text = "Event Schedule",
                    color = BassheadTheme.colors.onSurface,
                    modifier = Modifier.padding(start = BassheadTheme.spacing.medium),
                )
            }

            // Date text
            BassheadBodyLarge(
                text = dateString,
                color = BassheadTheme.colors.onSurface,
            )

            // Time info row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Time",
                    tint = BassheadTheme.colors.onSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
                BassheadBodyMedium(
                    text = "Check event details for exact timing",
                    color = BassheadTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(start = BassheadTheme.spacing.small),
                )
            }
        }
    }
}

/**
 * Description card molecule - Flattened hierarchy
 */
@Composable
fun BassheadFestivalDescriptionCardMolecule(
    description: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
        shape = RoundedCornerShape(BassheadTheme.spacing.medium),
    ) {
        Column(
            modifier = Modifier.padding(BassheadTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
        ) {
            BassheadTitleMedium(
                text = stringResource(Res.string.festival_about_title),
                color = BassheadTheme.colors.onSurface,
            )

            BassheadBodyMedium(
                text = description,
                color = BassheadTheme.colors.onSurface,
            )
        }
    }
}

/**
 * Stats card molecule - Flattened hierarchy
 */
@Composable
fun BassheadFestivalStatsCardMolecule(
    festival: FestivalItemState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = festival.userJoined,
        enter = slideInVertically() + fadeIn(),
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = BassheadTheme.colors.surface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
            shape = RoundedCornerShape(BassheadTheme.spacing.medium),
        ) {
            Column(
                modifier = Modifier.padding(BassheadTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
            ) {
                // Header row with icon and title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = stringResource(Res.string.festival_stats_icon_desc),
                        tint = BassheadTheme.colors.primary,
                        modifier = Modifier.size(24.dp),
                    )
                    BassheadTitleMedium(
                        text = stringResource(Res.string.festival_your_stats_title),
                        color = BassheadTheme.colors.onSurface,
                        modifier = Modifier.padding(start = BassheadTheme.spacing.small),
                    )
                }

                // Headbangs stat
                BassheadStatRowAtom(
                    icon = Icons.Default.MusicNote,
                    label = stringResource(Res.string.festival_total_headbangs_label),
                    value = "${festival.totalHeadbangs}",
                )

                // Rank stat (if available)
                festival.userRank?.let { userRank ->
                    BassheadStatRowAtom(
                        icon = Icons.Default.Star,
                        label = stringResource(Res.string.festival_your_rank_label),
                        value = "#$userRank",
                        valueColor = BassheadTheme.colors.secondary,
                    )
                }
            }
        }
    }
}

/**
 * Actions card molecule
 */
@Composable
fun BassheadFestivalActionsCardMolecule(
    festival: FestivalItemState,
    isJoining: Boolean,
    onAction: (FestivalDetailActions) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
        shape = RoundedCornerShape(BassheadTheme.spacing.medium),
    ) {
        if (!festival.userJoined) {
            BassheadLoadingButtonAtom(
                text = stringResource(Res.string.festival_join_button),
                onClick = { onAction(FestivalDetailActions.JoinFestival) },
                isLoading = isJoining,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BassheadTheme.spacing.medium),
            )
        } else {
            BassheadLoadingButtonAtom(
                text = stringResource(Res.string.festival_view_leaderboard_button),
                onClick = { onAction(FestivalDetailActions.ViewLeaderboard) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BassheadTheme.spacing.medium),
            )
        }
    }
}

/**
 * Error state molecule
 */
@Composable
fun BassheadFestivalErrorStateMolecule(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BassheadErrorStateAtom(
        error = error,
        onRetry = onRetry,
        onBack = onBack,
        modifier = modifier.fillMaxWidth(),
    )
}
