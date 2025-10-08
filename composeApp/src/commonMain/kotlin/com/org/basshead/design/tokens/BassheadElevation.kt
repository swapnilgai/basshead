package com.org.basshead.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Basshead elevation system following Material Design 3 elevation levels
 * Provides consistent depth hierarchy across the app
 */
@Immutable
data class BassheadElevation(
    // Standard elevation levels
    val level0: Dp, // Surface level
    val level1: Dp, // Elevated surface
    val level2: Dp, // Cards, menus
    val level3: Dp, // FAB, snackbar
    val level4: Dp, // Navigation drawer
    val level5: Dp, // Modal bottom sheet

    // Component-specific elevations
    val button: Dp,
    val card: Dp,
    val dialog: Dp,
    val navigationBar: Dp,
    val appBar: Dp,
    val bottomSheet: Dp,
    val menu: Dp,
    val tooltip: Dp,

    // Festival-specific elevations
    val festivalCard: Dp,
    val festivalDetail: Dp,
    val artistCard: Dp,
)

/**
 * Default elevation tokens for Basshead
 * Following Material Design 3 elevation system
 */
val BassheadElevationTokens = BassheadElevation(
    // Standard elevation levels
    level0 = 0.dp,
    level1 = 1.dp,
    level2 = 3.dp,
    level3 = 6.dp,
    level4 = 8.dp,
    level5 = 12.dp,

    // Component-specific elevations
    button = 1.dp,
    card = 1.dp,
    dialog = 24.dp,
    navigationBar = 3.dp,
    appBar = 0.dp, // Surface tint instead of elevation
    bottomSheet = 1.dp,
    menu = 3.dp,
    tooltip = 24.dp,

    // Festival-specific elevations
    festivalCard = 2.dp,
    festivalDetail = 0.dp,
    artistCard = 1.dp,
)
