package com.org.basshead.design.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadCard
import com.org.basshead.design.atoms.BassheadIcon
import com.org.basshead.design.atoms.BassheadOutlinedButton
import com.org.basshead.design.atoms.BassheadStatusIndicator
import com.org.basshead.design.atoms.BassheadFestivalName
import com.org.basshead.design.atoms.BassheadVenueText
import com.org.basshead.design.atoms.BassheadDateTime
import com.org.basshead.design.atoms.BassheadBodySmall
import com.org.basshead.design.atoms.BassheadLabelSmall
import com.org.basshead.design.theme.BassheadTheme

/**
 * Festival Card Organism - Complex component for displaying festival information
 * Follows Google Material Design 3 patterns with semantic states and accessibility
 */
@Composable
fun BassheadFestivalCard(
    festivalName: String,
    venue: String,
    dateTime: String,
    isUserJoined: Boolean,
    isActive: Boolean,
    participantCount: Int? = null,
    modifier: Modifier = Modifier,
    onFestivalClick: () -> Unit = {},
    onJoinClick: () -> Unit = {},
    onLeaderboardClick: () -> Unit = {},
) {
    // Semantic color selection based on festival state
    val festivalStatusColor = when {
        isActive && isUserJoined -> BassheadTheme.colors.festivalActive
        isActive -> BassheadTheme.colors.festivalUpcoming
        else -> BassheadTheme.colors.festivalPast
    }

    val festivalStatusContainerColor = when {
        isActive && isUserJoined -> BassheadTheme.colors.festivalActiveContainer
        isActive -> BassheadTheme.colors.festivalUpcomingContainer
        else -> BassheadTheme.colors.festivalPastContainer
    }

    BassheadCard(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Festival: $festivalName at $venue on $dateTime, ${if (isUserJoined) "joined" else "not joined"}"
            },
        shape = RoundedCornerShape(BassheadTheme.spacing.medium),
        elevation = BassheadTheme.elevation.festivalCard,
    ) {
        Column(
            modifier = Modifier.padding(BassheadTheme.spacing.festivalCardPadding),
            verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.festivalInfoSpacing)
        ) {
            // Header with festival status indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.extraSmall)
                ) {
                    // Festival name with status indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small)
                    ) {
                        BassheadStatusIndicator(
                            isActive = isActive,
                            size = 8.dp
                        )

                        BassheadFestivalName(
                            text = festivalName,
                            color = BassheadTheme.colors.onSurface,
                            maxLines = 2
                        )
                    }

                    // User participation badge
                    if (isUserJoined) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = festivalStatusContainerColor,
                            modifier = Modifier.padding(vertical = BassheadTheme.spacing.extraSmall)
                        ) {
                            BassheadLabelSmall(
                                text = "JOINED",
                                color = festivalStatusColor,
                                modifier = Modifier.padding(
                                    horizontal = BassheadTheme.spacing.small,
                                    vertical = BassheadTheme.spacing.extraSmall
                                )
                            )
                        }
                    }
                }

                // Star rating or favorite indicator (placeholder for future enhancement)
                BassheadIcon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Festival rating",
                    tint = BassheadTheme.colors.outline,
                    size = 20.dp
                )
            }

            // Festival details with semantic icons
            Column(
                verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.artistSpacing)
            ) {
                // Venue information
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small)
                ) {
                    BassheadIcon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = BassheadTheme.colors.onSurfaceVariant,
                        size = 16.dp
                    )

                    BassheadVenueText(
                        text = venue,
                        color = BassheadTheme.colors.onSurfaceVariant
                    )
                }

                // Date and time
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small)
                ) {
                    BassheadIcon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Date and time",
                        tint = BassheadTheme.colors.onSurfaceVariant,
                        size = 16.dp
                    )

                    BassheadDateTime(
                        text = dateTime,
                        color = BassheadTheme.colors.onSurfaceVariant
                    )
                }

                // Participant count if available
                participantCount?.let { count ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small)
                    ) {
                        BassheadIcon(
                            imageVector = Icons.Default.People,
                            contentDescription = "Participants",
                            tint = BassheadTheme.colors.onSurfaceVariant,
                            size = 16.dp
                        )

                        BassheadBodySmall(
                            text = "$count participants",
                            color = BassheadTheme.colors.onSurfaceVariant
                        )
                    }
                }
            }

            // Action buttons with contextual states
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isUserJoined) {
                    // Show leaderboard button for joined festivals
                    BassheadOutlinedButton(
                        text = "Leaderboard",
                        onClick = onLeaderboardClick,
                        modifier = Modifier.weight(1f)
                    )

                    BassheadButton(
                        text = "View Details",
                        onClick = onFestivalClick,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    // Show join option for non-joined festivals
                    BassheadOutlinedButton(
                        text = "Learn More",
                        onClick = onFestivalClick,
                        modifier = Modifier.weight(1f)
                    )

                    if (isActive) {
                        BassheadButton(
                            text = "Join Festival",
                            onClick = onJoinClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
