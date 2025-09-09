package com.org.basshead.design.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadDisplaySmall
import com.org.basshead.design.theme.BassheadTheme

/**
 * Molecular branding components
 * App branding combinations for consistent brand presentation
 */

/**
 * App logo molecule - reusable app logo with consistent sizing
 * Performance optimized: Direct icon composition, configurable size
 */
@Composable
fun BassheadAppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    tint: Color = BassheadTheme.colors.primary,
    icon: ImageVector = Icons.Default.Pets,
    contentDescription: String = "Basshead app logo"
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = modifier.size(size),
        tint = tint
    )
}

/**
 * App name molecule - consistent app name styling
 * Performance optimized: Themeable text component
 */
@Composable
fun BassheadAppName(
    modifier: Modifier = Modifier,
    text: String = "Basshead",
    color: Color = BassheadTheme.colors.primary
) {
    BassheadDisplaySmall(
        text = text,
        color = color,
        modifier = modifier
    )
}

/**
 * App tagline molecule - consistent tagline styling
 * Performance optimized: Themeable text component with alpha
 */
@Composable
fun BassheadAppTagline(
    modifier: Modifier = Modifier,
    text: String = "Feel the Bass, Join the Beat",
    color: Color = BassheadTheme.colors.onSurface.copy(alpha = 0.87f)
) {
    BassheadBodyLarge(
        text = text,
        color = color,
        modifier = modifier
    )
}
