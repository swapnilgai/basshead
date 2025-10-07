package com.org.basshead.design.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.featured_festival
import basshead.composeapp.generated.resources.no_festivals_available
import basshead.composeapp.generated.resources.welcome_default_user
import basshead.composeapp.generated.resources.welcome_user
import basshead.composeapp.generated.resources.your_current_festival
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadHeadlineLarge
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.dashboard.components.DeviceSyncCard
import com.org.basshead.feature.dashboard.components.FestivalItem
import com.org.basshead.feature.dashboard.model.FestivalItemState
import com.org.basshead.feature.dashboard.model.UserProfileState
import org.jetbrains.compose.resources.stringResource

/**
 * Atomic design organism for dashboard screen layout
 * Follows the same patterns as BassheadProfileScreenLayout with proper theme integration
 * Optimized with flattened hierarchy for maximum performance
 */
@Composable
fun BassheadDashboardScreenLayout(
    profile: UserProfileState?,
    totalHeadbangs: Int,
    isDeviceConnected: Boolean,
    isSyncing: Boolean,
    featuredFestival: FestivalItemState?,
    showNoFestivalsMessage: Boolean,
    onFestivalClick: (String) -> Unit,
    onJoinFestival: (String) -> Unit,
    onViewLeaderboard: (String) -> Unit,
    onSyncDevice: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = BassheadTheme.spacing.medium,
            vertical = BassheadTheme.spacing.large,
        ),
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.large),
    ) {
        // Welcome message - Direct composition
        item(key = "welcome_header") {
            BassheadHeadlineLarge(
                text = profile?.name?.let { name ->
                    stringResource(Res.string.welcome_user, name)
                } ?: stringResource(Res.string.welcome_default_user),
                color = BassheadTheme.colors.onSurface,
            )
        }

        // Device sync card - Direct composition
        item(key = "device_sync_card") {
            DeviceSyncCard(
                totalHeadbangs = totalHeadbangs,
                isDeviceConnected = isDeviceConnected,
                isSyncing = isSyncing,
                onSyncClick = onSyncDevice,
                onSettingsClick = onOpenSettings,
            )
        }

        // Featured Festival (user joined or first suggestion) - Direct composition
        featuredFestival?.let { festival ->
            item(key = "featured_festival_${festival.id}") {
                Column {
                    BassheadTitleMedium(
                        text = if (festival.userJoined) {
                            stringResource(Res.string.your_current_festival)
                        } else {
                            stringResource(Res.string.featured_festival)
                        },
                        color = BassheadTheme.colors.onSurface,
                        modifier = Modifier.padding(bottom = BassheadTheme.spacing.small),
                    )

                    FestivalItem(
                        festival = festival,
                        onFestivalClick = onFestivalClick,
                        onJoinFestival = onJoinFestival,
                        onViewLeaderboard = onViewLeaderboard,
                    )
                }
            }
        }

        // Show message if no festivals - Direct composition
        if (showNoFestivalsMessage) {
            item(key = "no_festivals_message") {
                BassheadBodyLarge(
                    text = stringResource(Res.string.no_festivals_available),
                    color = BassheadTheme.colors.onSurfaceVariant,
                    modifier = Modifier.padding(BassheadTheme.spacing.large),
                )
            }
        }
    }
}
