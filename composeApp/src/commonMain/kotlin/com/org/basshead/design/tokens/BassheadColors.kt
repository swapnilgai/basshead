package com.org.basshead.design.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Enhanced Basshead color palette following Google's Material Design 3 principles
 * Includes comprehensive semantic color system for accessibility and brand consistency
 */
@Immutable
data class BassheadColors(
    // Core brand colors - Material Design 3 primary system
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    // Secondary brand colors - supporting brand expression
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    // Tertiary colors - additional brand accent
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    // Error system colors
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    // Surface system colors
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val surfaceTint: Color,
    val surfaceDim: Color,
    val surfaceBright: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,

    // Background colors
    val background: Color,
    val onBackground: Color,

    // Outline system
    val outline: Color,
    val outlineVariant: Color,

    // Inverse colors for high contrast scenarios
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val inversePrimary: Color,

    // Scrim for modals and overlays
    val scrim: Color,

    // Enhanced semantic color system for music festivals
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,

    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,

    val info: Color,
    val onInfo: Color,
    val infoContainer: Color,
    val onInfoContainer: Color,

    // Festival lifecycle states with enhanced semantics
    val festivalActive: Color,
    val festivalActiveContainer: Color,
    val onFestivalActive: Color,
    val onFestivalActiveContainer: Color,

    val festivalInactive: Color,
    val festivalInactiveContainer: Color,
    val onFestivalInactive: Color,
    val onFestivalInactiveContainer: Color,

    val festivalUpcoming: Color,
    val festivalUpcomingContainer: Color,
    val onFestivalUpcoming: Color,
    val onFestivalUpcomingContainer: Color,

    val festivalPast: Color,
    val festivalPastContainer: Color,
    val onFestivalPast: Color,
    val onFestivalPastContainer: Color,

    // Music-specific semantic colors
    val bassIntense: Color,
    val bassIntenseContainer: Color,
    val onBassIntense: Color,
    val onBassIntenseContainer: Color,

    val deviceConnected: Color,
    val deviceConnectedContainer: Color,
    val onDeviceConnected: Color,
    val onDeviceConnectedContainer: Color,

    val deviceDisconnected: Color,
    val deviceDisconnectedContainer: Color,
    val onDeviceDisconnected: Color,
    val onDeviceDisconnectedContainer: Color,
)

/**
 * Enhanced light theme colors with comprehensive Material Design 3 surface system
 */
val BassheadLightColors = BassheadColors(
    // Primary - Deep purple brand identity
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEADDFF),
    onPrimaryContainer = Color(0xFF21005D),

    // Secondary - Complementary teal
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),

    // Tertiary - Warm accent
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    // Error system
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Enhanced surface system
    surface = Color(0xFFFEF7FF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceTint = Color(0xFF6750A4),
    surfaceDim = Color(0xFFDED8E1),
    surfaceBright = Color(0xFFFEF7FF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F2FA),
    surfaceContainer = Color(0xFFF1ECF4),
    surfaceContainerHigh = Color(0xFFECE6F0),
    surfaceContainerHighest = Color(0xFFE6E0E9),

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

    // Enhanced semantic colors
    success = Color(0xFF146C2E),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFA6F2AA),
    onSuccessContainer = Color(0xFF002204),

    warning = Color(0xFF8C5000),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFDDB3),
    onWarningContainer = Color(0xFF2D1600),

    info = Color(0xFF0061A4),
    onInfo = Color(0xFFFFFFFF),
    infoContainer = Color(0xFFD1E4FF),
    onInfoContainer = Color(0xFF001D36),

    // Festival states with container support
    festivalActive = Color(0xFF146C2E),
    festivalActiveContainer = Color(0xFFA6F2AA),
    onFestivalActive = Color(0xFFFFFFFF),
    onFestivalActiveContainer = Color(0xFF002204),

    festivalInactive = Color(0xFF5F5F5F),
    festivalInactiveContainer = Color(0xFFE3E3E3),
    onFestivalInactive = Color(0xFFFFFFFF),
    onFestivalInactiveContainer = Color(0xFF1C1C1C),

    festivalUpcoming = Color(0xFF0061A4),
    festivalUpcomingContainer = Color(0xFFD1E4FF),
    onFestivalUpcoming = Color(0xFFFFFFFF),
    onFestivalUpcomingContainer = Color(0xFF001D36),

    festivalPast = Color(0xFF6F6F6F),
    festivalPastContainer = Color(0xFFF5F5F5),
    onFestivalPast = Color(0xFFFFFFFF),
    onFestivalPastContainer = Color(0xFF262626),

    // Music-specific colors
    bassIntense = Color(0xFF8B0000),
    bassIntenseContainer = Color(0xFFFFD6D6),
    onBassIntense = Color(0xFFFFFFFF),
    onBassIntenseContainer = Color(0xFF370000),

    deviceConnected = Color(0xFF146C2E),
    deviceConnectedContainer = Color(0xFFA6F2AA),
    onDeviceConnected = Color(0xFFFFFFFF),
    onDeviceConnectedContainer = Color(0xFF002204),

    deviceDisconnected = Color(0xFF8C1538),
    deviceDisconnectedContainer = Color(0xFFFFD9E2),
    onDeviceDisconnected = Color(0xFFFFFFFF),
    onDeviceDisconnectedContainer = Color(0xFF3E0A16),
)

/**
 * Enhanced dark theme colors optimized for low-light festival environments
 */
val BassheadDarkColors = BassheadColors(
    // Primary system for dark theme
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),

    // Secondary system
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),

    // Tertiary system
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    // Error system
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Dark surface system
    surface = Color(0xFF10121B),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceTint = Color(0xFFD0BCFF),
    surfaceDim = Color(0xFF10121B),
    surfaceBright = Color(0xFF362F42),
    surfaceContainerLowest = Color(0xFF0B0E14),
    surfaceContainerLow = Color(0xFF1D1B20),
    surfaceContainer = Color(0xFF211F26),
    surfaceContainerHigh = Color(0xFF2B2930),
    surfaceContainerHighest = Color(0xFF36343B),

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

    // Enhanced semantic colors for dark theme
    success = Color(0xFF8BDA8F),
    onSuccess = Color(0xFF003909),
    successContainer = Color(0xFF00531C),
    onSuccessContainer = Color(0xFFA6F2AA),

    warning = Color(0xFFFFB951),
    onWarning = Color(0xFF4A2800),
    warningContainer = Color(0xFF6B3C00),
    onWarningContainer = Color(0xFFFFDDB3),

    info = Color(0xFF9ECAFF),
    onInfo = Color(0xFF003258),
    infoContainer = Color(0xFF00497D),
    onInfoContainer = Color(0xFFD1E4FF),

    // Festival states optimized for dark theme
    festivalActive = Color(0xFF8BDA8F),
    festivalActiveContainer = Color(0xFF00531C),
    onFestivalActive = Color(0xFF003909),
    onFestivalActiveContainer = Color(0xFFA6F2AA),

    festivalInactive = Color(0xFF8E8E8E),
    festivalInactiveContainer = Color(0xFF404040),
    onFestivalInactive = Color(0xFF1C1C1C),
    onFestivalInactiveContainer = Color(0xFFE3E3E3),

    festivalUpcoming = Color(0xFF9ECAFF),
    festivalUpcomingContainer = Color(0xFF00497D),
    onFestivalUpcoming = Color(0xFF003258),
    onFestivalUpcomingContainer = Color(0xFFD1E4FF),

    festivalPast = Color(0xFF999999),
    festivalPastContainer = Color(0xFF2F2F2F),
    onFestivalPast = Color(0xFF262626),
    onFestivalPastContainer = Color(0xFFF5F5F5),

    // Music-specific colors for dark theme
    bassIntense = Color(0xFFFF6B6B),
    bassIntenseContainer = Color(0xFF5F0000),
    onBassIntense = Color(0xFF370000),
    onBassIntenseContainer = Color(0xFFFFD6D6),

    deviceConnected = Color(0xFF8BDA8F),
    deviceConnectedContainer = Color(0xFF00531C),
    onDeviceConnected = Color(0xFF003909),
    onDeviceConnectedContainer = Color(0xFFA6F2AA),

    deviceDisconnected = Color(0xFFFFB2C5),
    deviceDisconnectedContainer = Color(0xFF633244),
    onDeviceDisconnected = Color(0xFF3E0A16),
    onDeviceDisconnectedContainer = Color(0xFFFFD9E2),
)
