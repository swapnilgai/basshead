package com.org.basshead.feature.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.device_status_connected
import basshead.composeapp.generated.resources.device_status_not_connected
import basshead.composeapp.generated.resources.settings
import basshead.composeapp.generated.resources.sync_now
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadDisplayLarge
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeviceSyncCard(
    totalHeadbangs: Int,
    isDeviceConnected: Boolean,
    isSyncing: Boolean,
    onSyncClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Left: Device icon with subtle background
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = "Device",
                    tint = BassheadTheme.colors.primary,
                    modifier = Modifier.size(28.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Center: Headbangs count and status - takes available space
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                // Headbangs count using atomic design typography
                BassheadDisplayLarge(
                    text = totalHeadbangs.toString(),
                    color = BassheadTheme.colors.onSurface,
                    maxLines = 1,
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Device status with improved design using atomic components
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    // Status indicator dot
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                color = if (isDeviceConnected) Color(0xFF4CAF50) else Color(0xFFFF9800),
                                shape = CircleShape,
                            ),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    BassheadBodyMedium(
                        text = if (isDeviceConnected) {
                            stringResource(Res.string.device_status_connected)
                        } else {
                            stringResource(Res.string.device_status_not_connected)
                        },
                        color = if (isDeviceConnected) Color(0xFF2E7D32) else Color(0xFFE65100),
                        maxLines = 1,
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right: Action buttons with circular backgrounds
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Sync button with circular tinted background
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = if (isDeviceConnected && !isSyncing) {
                                BassheadTheme.colors.primary.copy(alpha = 0.1f)
                            } else {
                                BassheadTheme.colors.onSurface.copy(alpha = 0.05f)
                            },
                            shape = CircleShape,
                        ),
                ) {
                    IconButton(
                        onClick = onSyncClick,
                        enabled = isDeviceConnected && !isSyncing,
                        modifier = Modifier.size(44.dp),
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = BassheadTheme.colors.primary,
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = stringResource(Res.string.sync_now),
                                tint = if (isDeviceConnected) {
                                    BassheadTheme.colors.primary
                                } else {
                                    BassheadTheme.colors.onSurface.copy(alpha = 0.4f)
                                },
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }

                // Settings button with circular tinted background
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = BassheadTheme.colors.secondary.copy(alpha = 0.1f),
                            shape = CircleShape,
                        ),
                ) {
                    IconButton(
                        onClick = onSettingsClick,
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(Res.string.settings),
                            tint = BassheadTheme.colors.secondary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
            }
        }
    }
}
