package com.org.basshead.design.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.org.basshead.design.constants.FestivalConstants
import com.org.basshead.design.theme.BassheadTheme

/**
 * Atomic components for Festival Detail Screen
 * Following BassheadTheme design system
 */

/**
 * Info card atom for displaying stats with icon, value, and label
 */
@Composable
fun BassheadInfoCardAtom(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    valueColor: Color = BassheadTheme.colors.onSurface,
    iconTint: Color = BassheadTheme.colors.primary,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = BassheadTheme.colors.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = BassheadTheme.elevation.level1),
        shape = RoundedCornerShape(BassheadTheme.spacing.medium),
    ) {
        Column(
            modifier = Modifier.padding(BassheadTheme.spacing.large), // Increased from medium to large
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp), // Increased from 24dp
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium)) // Increased from small

            BassheadTitleMedium(
                text = value,
                color = valueColor,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(BassheadTheme.spacing.small)) // Added spacing

            BassheadBodyMedium( // Changed from BodySmall to BodyMedium for better readability
                text = label,
                color = BassheadTheme.colors.onSurfaceVariant, // Better contrast color
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Status badge atom for displaying festival status
 */
@Composable
fun BassheadStatusBadgeAtom(
    status: String,
    modifier: Modifier = Modifier,
) {
    val (statusColor, textColor) = FestivalConstants.getStatusColors(status)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = statusColor,
        modifier = modifier,
    ) {
        BassheadBodyMedium(
            text = status.uppercase(),
            color = textColor,
            modifier = Modifier.padding(
                horizontal = BassheadTheme.spacing.medium,
                vertical = BassheadTheme.spacing.small,
            ),
        )
    }
}

/**
 * Stat row atom for displaying a statistic with icon and value
 */
@Composable
fun BassheadStatRowAtom(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    iconTint: Color = BassheadTheme.colors.primary,
    valueColor: Color = BassheadTheme.colors.primary,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(BassheadTheme.spacing.small))
            BassheadBodyLarge(
                text = label,
                color = BassheadTheme.colors.outline,
            )
        }

        BassheadBodyLarge(
            text = value,
            color = valueColor,
        )
    }
}

/**
 * Loading button atom that shows loading state
 */
@Composable
fun BassheadLoadingButtonAtom(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    BassheadButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        isLoading = isLoading,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = BassheadTheme.colors.onPrimary,
                strokeWidth = 2.dp,
            )
            Spacer(modifier = Modifier.width(BassheadTheme.spacing.small))
        }
        BassheadBodyLarge(
            text = text,
            color = BassheadTheme.colors.onPrimary,
        )
    }
}

/**
 * Error state atom for displaying error messages
 */
@Composable
fun BassheadErrorStateAtom(
    error: String,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(BassheadTheme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        BassheadHeadlineMedium(
            text = "Oops!",
            color = BassheadTheme.colors.error,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.small))

        BassheadBodyLarge(
            text = error,
            textAlign = TextAlign.Center,
            color = BassheadTheme.colors.outline,
        )

        Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraLarge))

        Row(
            horizontalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
        ) {
            BassheadOutlinedButton(onClick = onBack) {
                BassheadBodyLarge(
                    text = "Go Back",
                    color = BassheadTheme.colors.primary,
                )
            }

            BassheadOutlinedButton(onClick = onRetry) {
                BassheadBodyLarge(
                    text = "Retry",
                    color = BassheadTheme.colors.primary,
                )
            }
        }
    }
}
