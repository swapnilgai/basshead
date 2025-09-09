package com.org.basshead.design.atoms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.basshead.design.theme.BassheadTheme

/**
 * Atomic visual indicator components
 * Base visual elements for consistent UI patterns
 */

/**
 * Loading indicator atom
 */
@Composable
fun BassheadLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    strokeWidth: Dp = 4.dp,
    color: Color = BassheadTheme.colors.primary,
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        strokeWidth = strokeWidth,
        color = color,
    )
}

/**
 * Icon atom with consistent sizing and theming
 */
@Composable
fun BassheadIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = BassheadTheme.colors.onSurface,
    size: Dp = 24.dp,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint,
    )
}

/**
 * Surface atom for consistent elevation and theming
 */
@Composable
fun BassheadSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    color: Color = BassheadTheme.colors.surface,
    contentColor: Color = BassheadTheme.colors.onSurface,
    tonalElevation: Dp = BassheadTheme.elevation.level1,
    shadowElevation: Dp = BassheadTheme.elevation.level1,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = color,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        content = content,
    )
}

/**
 * Card atom for consistent card styling
 */
@Composable
fun BassheadCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(12.dp),
    elevation: Dp = BassheadTheme.elevation.card,
    colors: androidx.compose.material3.CardColors = CardDefaults.cardColors(
        containerColor = BassheadTheme.colors.surface,
        contentColor = BassheadTheme.colors.onSurface,
    ),
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier,
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = colors,
        content = content,
    )
}

/**
 * Divider atoms for consistent separation
 */
@Composable
fun BassheadHorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = BassheadTheme.colors.outlineVariant,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Composable
fun BassheadVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = BassheadTheme.colors.outlineVariant,
) {
    VerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

/**
 * Status indicator atom for festival states
 */
@Composable
fun BassheadStatusIndicator(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) BassheadTheme.colors.festivalActive else BassheadTheme.colors.festivalInactive,
            modifier = Modifier.size(size),
        ) {}
    }
}
