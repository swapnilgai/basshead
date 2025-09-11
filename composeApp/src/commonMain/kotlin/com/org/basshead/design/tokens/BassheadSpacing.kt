package com.org.basshead.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Basshead spacing system following 4dp grid system
 * Based on Material Design principles with festival-specific spacing needs
 */
@Immutable
data class BassheadSpacing(
    // Base spacing units
    val none: Dp,
    val extraSmall: Dp,
    val small: Dp,
    val medium: Dp,
    val large: Dp,
    val extraLarge: Dp,
    val extraExtraLarge: Dp,

    // Component-specific spacing
    val buttonPadding: Dp,
    val cardPadding: Dp,
    val screenPadding: Dp,
    val sectionSpacing: Dp,
    val itemSpacing: Dp,

    // List and grid spacing
    val listItemSpacing: Dp,
    val gridSpacing: Dp,

    // Form spacing
    val formFieldSpacing: Dp,
    val formSectionSpacing: Dp,

    // Festival-specific spacing
    val festivalCardPadding: Dp,
    val festivalInfoSpacing: Dp,
    val artistSpacing: Dp,
)

/**
 * Default spacing tokens for Basshead
 * Following 4dp grid system for consistency
 */
val BassheadSpacingTokens = BassheadSpacing(
    // Base spacing units (4dp grid)
    none = 0.dp,
    extraSmall = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 24.dp,
    extraLarge = 32.dp,
    extraExtraLarge = 48.dp,

    // Component-specific spacing
    buttonPadding = 16.dp,
    cardPadding = 16.dp,
    screenPadding = 16.dp,
    sectionSpacing = 24.dp,
    itemSpacing = 12.dp,

    // List and grid spacing
    listItemSpacing = 8.dp,
    gridSpacing = 12.dp,

    // Form spacing
    formFieldSpacing = 16.dp,
    formSectionSpacing = 32.dp,

    // Festival-specific spacing
    festivalCardPadding = 20.dp,
    festivalInfoSpacing = 12.dp,
    artistSpacing = 8.dp,
)
