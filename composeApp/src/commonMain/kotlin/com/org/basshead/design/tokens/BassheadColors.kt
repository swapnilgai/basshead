package com.org.basshead.design.tokens

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
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

    // Device connectivity states
    val deviceConnected: Color,
    val onDeviceConnected: Color,
    val deviceConnectedContainer: Color,
    val onDeviceConnectedContainer: Color,

    val deviceDisconnected: Color,
    val onDeviceDisconnected: Color,
    val deviceDisconnectedContainer: Color,
    val onDeviceDisconnectedContainer: Color,

    // Bass/Music intensity colors
    val bassIntense: Color,
    val onBassIntense: Color,
    val bassIntenseContainer: Color,
    val onBassIntenseContainer: Color,

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
) {
    /**
     * Converts BassheadColors to Material 3 ColorScheme for seamless integration
     * This enables compatibility with Material 3 components while maintaining brand colors
     */
    fun toMaterialColorScheme(): ColorScheme = lightColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        inversePrimary = inversePrimary,
        surfaceDim = surfaceDim,
        surfaceBright = surfaceBright,
        surfaceContainerLowest = surfaceContainerLowest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
    )

    /**
     * Converts BassheadColors to Material 3 Dark ColorScheme for seamless integration
     */
    fun toMaterialDarkColorScheme(): ColorScheme = darkColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        inversePrimary = inversePrimary,
        surfaceDim = surfaceDim,
        surfaceBright = surfaceBright,
        surfaceContainerLowest = surfaceContainerLowest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
    )
}

/**
 * Material Design 3 Light Theme Colors for Basshead
 * Following Google's color guidelines for accessibility and brand consistency
 */
val BassheadLightColors = BassheadColors(
    // Primary colors - Bass/Music theme with high contrast
    primary = Color(0xFF6B4EFF), // Deep purple for bass vibes
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE4DFFF),
    onPrimaryContainer = Color(0xFF21005E),

    // Secondary colors - Electric/Energy theme
    secondary = Color(0xFF625B71),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1E192B),

    // Tertiary colors - Beat/Rhythm theme
    tertiary = Color(0xFF7D5260),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF370B1E),

    // Error system
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Surface system - Light theme surfaces
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    surfaceTint = Color(0xFF6B4EFF),
    surfaceDim = Color(0xFFDDD8E1),
    surfaceBright = Color(0xFFFFFBFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F2FA),
    surfaceContainer = Color(0xFFF1ECF4),
    surfaceContainerHigh = Color(0xFFEBE6EE),
    surfaceContainerHighest = Color(0xFFE6E0E9),

    // Background
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1C1B1F),

    // Outline system
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),

    // Inverse colors
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFC5BFFF),

    // Scrim
    scrim = Color(0xFF000000),

    // Enhanced semantic colors
    success = Color(0xFF146C2E),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFA6F3A6),
    onSuccessContainer = Color(0xFF002110),

    warning = Color(0xFF8C5000),
    onWarning = Color(0xFFFFFFFF),
    warningContainer = Color(0xFFFFDDB4),
    onWarningContainer = Color(0xFF2E1500),

    info = Color(0xFF0061A6),
    onInfo = Color(0xFFFFFFFF),
    infoContainer = Color(0xFFD3E4FF),
    onInfoContainer = Color(0xFF001C38),

    // Device connectivity colors
    deviceConnected = Color(0xFF00C853),
    onDeviceConnected = Color(0xFFFFFFFF),
    deviceConnectedContainer = Color(0xFFB2FBC2),
    onDeviceConnectedContainer = Color(0xFF00391A),

    deviceDisconnected = Color(0xFFB0BEC5),
    onDeviceDisconnected = Color(0xFF1C1B1F),
    deviceDisconnectedContainer = Color(0xFFE1F5FE),
    onDeviceDisconnectedContainer = Color(0xFF102027),

    // Bass/Music intensity colors
    bassIntense = Color(0xFFFF5722),
    onBassIntense = Color(0xFFFFFFFF),
    bassIntenseContainer = Color(0xFFFFCCBC),
    onBassIntenseContainer = Color(0xFF3E2723),

    // Festival states - Light theme
    festivalActive = Color(0xFF00C853), // Bright green for active festivals
    festivalActiveContainer = Color(0xFFB8F5C8),
    onFestivalActive = Color(0xFFFFFFFF),
    onFestivalActiveContainer = Color(0xFF002111),

    festivalInactive = Color(0xFF78909C), // Gray for inactive
    festivalInactiveContainer = Color(0xFFE1F5FE),
    onFestivalInactive = Color(0xFFFFFFFF),
    onFestivalInactiveContainer = Color(0xFF102027),

    festivalUpcoming = Color(0xFFFF9800), // Orange for upcoming
    festivalUpcomingContainer = Color(0xFFFFE0B2),
    onFestivalUpcoming = Color(0xFFFFFFFF),
    onFestivalUpcomingContainer = Color(0xFF2E1500),

    festivalPast = Color(0xFF9C27B0), // Purple for past events
    festivalPastContainer = Color(0xFFE1BEE7),
    onFestivalPast = Color(0xFFFFFFFF),
    onFestivalPastContainer = Color(0xFF4A148C),
)

/**
 * Material Design 3 Dark Theme Colors for Basshead
 * Optimized for low-light environments and festival/club settings
 */
val BassheadDarkColors = BassheadColors(
    // Primary colors - Bass/Music theme with high contrast for dark
    primary = Color(0xFFC5BFFF), // Lighter purple for dark theme
    onPrimary = Color(0xFF37006F),
    primaryContainer = Color(0xFF512D8B),
    onPrimaryContainer = Color(0xFFE4DFFF),

    // Secondary colors - Electric/Energy theme
    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),

    // Tertiary colors - Beat/Rhythm theme
    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF4D2532),
    tertiaryContainer = Color(0xFF663B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    // Error system
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Surface system - Dark theme surfaces
    surface = Color(0xFF101014), // Very dark for festival/club feel
    onSurface = Color(0xFFE6E0E9),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceTint = Color(0xFFC5BFFF),
    surfaceDim = Color(0xFF101014),
    surfaceBright = Color(0xFF37343A),
    surfaceContainerLowest = Color(0xFF0B0B0F),
    surfaceContainerLow = Color(0xFF1C1B1F),
    surfaceContainer = Color(0xFF201F23),
    surfaceContainerHigh = Color(0xFF2B292D),
    surfaceContainerHighest = Color(0xFF363438),

    // Background
    background = Color(0xFF101014),
    onBackground = Color(0xFFE6E0E9),

    // Outline system
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),

    // Inverse colors
    inverseSurface = Color(0xFFE6E0E9),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Color(0xFF6B4EFF),

    // Scrim
    scrim = Color(0xFF000000),

    // Enhanced semantic colors - Dark theme variants
    success = Color(0xFF8BDB8B),
    onSuccess = Color(0xFF003919),
    successContainer = Color(0xFF005321),
    onSuccessContainer = Color(0xFFA6F3A6),

    warning = Color(0xFFFFB877),
    onWarning = Color(0xFF4A2800),
    warningContainer = Color(0xFF6A3C00),
    onWarningContainer = Color(0xFFFFDDB4),

    info = Color(0xFFA2C9FF),
    onInfo = Color(0xFF003062),
    infoContainer = Color(0xFF004B87),
    onInfoContainer = Color(0xFFD3E4FF),

    // Device connectivity colors
    deviceConnected = Color(0xFF69F285), // Bright neon green for dark
    onDeviceConnected = Color(0xFF003919),
    deviceConnectedContainer = Color(0xFF005321),
    onDeviceConnectedContainer = Color(0xFF8BDB8B),

    deviceDisconnected = Color(0xFFB0BEC5), // Lighter gray for dark
    onDeviceDisconnected = Color(0xFF263238),
    deviceDisconnectedContainer = Color(0xFF37474F),
    onDeviceDisconnectedContainer = Color(0xFFCFD8DC),

    // Bass/Music intensity colors
    bassIntense = Color(0xFFFF8A65), // Lighter shade for dark theme
    onBassIntense = Color(0xFF3E2723),
    bassIntenseContainer = Color(0xFFBF360C),
    onBassIntenseContainer = Color(0xFFFFCCBC),

    // Festival states - Dark theme optimized
    festivalActive = Color(0xFF69F285), // Bright neon green for dark
    festivalActiveContainer = Color(0xFF005321),
    onFestivalActive = Color(0xFF003919),
    onFestivalActiveContainer = Color(0xFF8BDB8B),

    festivalInactive = Color(0xFFB0BEC5), // Lighter gray for dark
    festivalInactiveContainer = Color(0xFF37474F),
    onFestivalInactive = Color(0xFF263238),
    onFestivalInactiveContainer = Color(0xFFCFD8DC),

    festivalUpcoming = Color(0xFFFFCC80), // Softer orange for dark
    festivalUpcomingContainer = Color(0xFF6A3C00),
    onFestivalUpcoming = Color(0xFF4A2800),
    onFestivalUpcomingContainer = Color(0xFFFFDDB4),

    festivalPast = Color(0xFFCE93D8), // Softer purple for dark
    festivalPastContainer = Color(0xFF7B1FA2),
    onFestivalPast = Color(0xFF4A148C),
    onFestivalPastContainer = Color(0xFFE1BEE7),
)

/**
 * Creates a Material 3 ColorScheme from BassheadColors
 * Following Material Design 3 guidelines for theme consistency
 */
fun BassheadColors.toColorScheme(isDark: Boolean = false): ColorScheme {
    return if (isDark) toMaterialDarkColorScheme() else toMaterialColorScheme()
}

/**
 * Factory function to create light ColorScheme from BassheadLightColors
 */
fun bassheadLightColorScheme(): ColorScheme = BassheadLightColors.toMaterialColorScheme()

/**
 * Factory function to create dark ColorScheme from BassheadDarkColors
 */
fun bassheadDarkColorScheme(): ColorScheme = BassheadDarkColors.toMaterialDarkColorScheme()

/**
 * Reactive factory function that returns appropriate ColorScheme based on theme
 */
fun bassheadColorScheme(isDark: Boolean = false): ColorScheme {
    return if (isDark) bassheadDarkColorScheme() else bassheadLightColorScheme()
}
