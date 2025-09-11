package com.org.basshead.design.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadCard
import com.org.basshead.design.atoms.BassheadIcon
import com.org.basshead.design.atoms.BassheadIconButton
import com.org.basshead.design.atoms.BassheadLabelMedium
import com.org.basshead.design.atoms.BassheadLoadingIndicator
import com.org.basshead.design.atoms.BassheadStatusIndicator
import com.org.basshead.design.atoms.BassheadTitleMedium
import com.org.basshead.design.theme.BassheadTheme

/**
 * Device Sync Card Organism - Complex component for device connection status and syncing
 * Follows Google Material Design 3 patterns with semantic color usage and accessibility
 */
@Composable
fun BassheadDeviceSyncCard(
    totalHeadbangs: Int,
    isDeviceConnected: Boolean,
    isSyncing: Boolean,
    modifier: Modifier = Modifier,
    onSyncClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    // Semantic color selection based on device state
    val deviceStatusColor = when {
        isSyncing -> BassheadTheme.colors.warning
        isDeviceConnected -> BassheadTheme.colors.deviceConnected
        else -> BassheadTheme.colors.deviceDisconnected
    }

    val deviceStatusContainerColor = when {
        isSyncing -> BassheadTheme.colors.warningContainer
        isDeviceConnected -> BassheadTheme.colors.deviceConnectedContainer
        else -> BassheadTheme.colors.deviceDisconnectedContainer
    }

    val deviceStatusText = when {
        isSyncing -> "Syncing..."
        isDeviceConnected -> "Device Connected"
        else -> "Device Not Connected"
    }

    BassheadCard(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "Device sync card showing $totalHeadbangs headbangs, status: $deviceStatusText"
            },
        shape = RoundedCornerShape(BassheadTheme.spacing.medium),
        elevation = BassheadTheme.elevation.card,
    ) {
        Column(
            modifier = Modifier.padding(BassheadTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
        ) {
            // Header row with device icon and settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
                ) {
                    // Device status indicator with semantic colors
                    Surface(
                        shape = RoundedCornerShape(BassheadTheme.spacing.small),
                        color = deviceStatusContainerColor,
                        modifier = Modifier.padding(BassheadTheme.spacing.extraSmall),
                    ) {
                        BassheadIcon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Device status",
                            tint = deviceStatusColor,
                            modifier = Modifier.padding(BassheadTheme.spacing.extraSmall),
                        )
                    }

                    BassheadTitleMedium(
                        text = "Device Sync",
                        color = BassheadTheme.colors.onSurface,
                    )
                }

                BassheadIconButton(
                    onClick = onSettingsClick,
                    icon = Icons.Default.Settings,
                    contentDescription = "Open device settings",
                )
            }

            // Headbangs counter with enhanced typography hierarchy
            Column(
                verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.extraSmall),
            ) {
                BassheadLabelMedium(
                    text = "TOTAL HEADBANGS",
                    color = BassheadTheme.colors.onSurfaceVariant,
                )

                BassheadTitleMedium(
                    text = totalHeadbangs.toString(),
                    color = BassheadTheme.colors.bassIntense,
                )
            }

            // Device status section with enhanced semantic feedback
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
                ) {
                    BassheadStatusIndicator(
                        isActive = isDeviceConnected && !isSyncing,
                        size = 8.dp,
                    )

                    BassheadBodyMedium(
                        text = deviceStatusText,
                        color = deviceStatusColor,
                    )
                }

                // Sync action with contextual states
                if (isSyncing) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small),
                    ) {
                        BassheadLoadingIndicator(
                            size = 16.dp,
                            color = BassheadTheme.colors.warning,
                        )
                        BassheadLabelMedium(
                            text = "Syncing",
                            color = BassheadTheme.colors.warning,
                        )
                    }
                } else {
                    BassheadButton(
                        text = if (isDeviceConnected) "Sync Now" else "Connect Device",
                        onClick = onSyncClick,
                        enabled = true,
                        leadingIcon = Icons.Default.Sync,
                        modifier = Modifier.height(36.dp),
                    )
                }
            }
        }
    }
}
