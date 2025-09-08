package com.org.basshead.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Basshead color palette following Material Design 3 principles
 * Based on Reddit's design system architecture with semantic color naming
 */
@Immutable
data class BassheadColors(
    // Primary colors - main brand colors
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    // Secondary colors - accent colors
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    // Tertiary colors - additional accent
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    // Error colors
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    // Surface colors
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,

    // Background colors
    val background: Color,
    val onBackground: Color,

    // Outline colors
    val outline: Color,
    val outlineVariant: Color,

    // Inverse colors
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,

    // Scrim
    val scrim: Color,

    // Custom semantic colors specific to Basshead
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    // Festival-specific colors
    val festivalActive: Color,
    val festivalInactive: Color,
    val festivalUpcoming: Color,
    val festivalPast: Color,
)

/**
 * Light theme colors
 */
val BassheadLightColors = BassheadColors(
    // Primary - Deep purple brand color
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),

    // Secondary - Teal accent
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),

    // Tertiary - Orange accent
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    // Error
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Surface
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceTint = Color(0xFF6750A4),

    // Background
    background = Color(0xFFFEF7FF),
    onBackground = Color(0xFF1C1B1F),

    // Outline
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),

    // Inverse
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFD0BCFF),

    // Scrim
    scrim = Color(0xFF000000),

    // Custom semantic colors
    success = Color(0xFF4CAF50),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFE8F5E8),
    onSuccessContainer = Color(0xFF1B5E20),

    warning = Color(0xFFFF9800),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFF3E0),
    onWarningContainer = Color(0xFFE65100),

    // Festival-specific
    festivalActive = Color(0xFF4CAF50),
    festivalInactive = Color(0xFF9E9E9E),
    festivalUpcoming = Color(0xFF2196F3),
    festivalPast = Color(0xFF757575),
)

/**
 * Dark theme colors
 */
val BassheadDarkColors = BassheadColors(
    // Primary
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),

    // Secondary
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),

    // Tertiary
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    // Error
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Surface
    surface = Color(0xFF10121B),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceTint = Color(0xFFD0BCFF),

    // Background
    background = Color(0xFF10121B),
    onBackground = Color(0xFFE6E1E5),

    // Outline
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    // Inverse
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Color(0xFF6750A4),

    // Scrim
    scrim = Color(0xFF000000),

    // Custom semantic colors
    success = Color(0xFF81C784),
    onSuccess = Color(0xFF2E7D32),
    successContainer = Color(0xFF1B5E20),
    onSuccessContainer = Color(0xFFC8E6C9),

    warning = Color(0xFFFFB74D),
    onWarning = Color(0xFFE65100),
    warningContainer = Color(0xFFBF360C),
    onWarningContainer = Color(0xFFFFE0B2),

    // Festival-specific
    festivalActive = Color(0xFF81C784),
    festivalInactive = Color(0xFF616161),
    festivalUpcoming = Color(0xFF64B5F6),
    festivalPast = Color(0xFF424242),
)
