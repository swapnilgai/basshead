package com.org.basshead.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.org.basshead.design.tokens.BassheadColors
import com.org.basshead.design.tokens.BassheadDarkColors
import com.org.basshead.design.tokens.BassheadElevation
import com.org.basshead.design.tokens.BassheadElevationTokens
import com.org.basshead.design.tokens.BassheadLightColors
import com.org.basshead.design.tokens.BassheadSpacing
import com.org.basshead.design.tokens.BassheadSpacingTokens
import com.org.basshead.design.tokens.BassheadTypography
import com.org.basshead.design.tokens.BassheadTypographyTokens

/**
 * CompositionLocal providers for design tokens
 * Following Reddit's approach with staticCompositionLocalOf for performance
 */
val LocalBassheadColors = staticCompositionLocalOf<BassheadColors> {
    noLocalProvidedFor("LocalBassheadColors")
}

val LocalBassheadTypography = staticCompositionLocalOf<BassheadTypography> {
    noLocalProvidedFor("LocalBassheadTypography")
}

val LocalBassheadSpacing = staticCompositionLocalOf<BassheadSpacing> {
    noLocalProvidedFor("LocalBassheadSpacing")
}

val LocalBassheadElevation = staticCompositionLocalOf<BassheadElevation> {
    noLocalProvidedFor("LocalBassheadElevation")
}

/**
 * Main theme object for atomic design system
 * Provides access to all design tokens for atomic components
 */
object BassheadTheme {
    val colors: BassheadColors
        @Composable
        @ReadOnlyComposable
        get() = LocalBassheadColors.current

    val typography: BassheadTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalBassheadTypography.current

    val spacing: BassheadSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalBassheadSpacing.current

    val elevation: BassheadElevation
        @Composable
        @ReadOnlyComposable
        get() = LocalBassheadElevation.current
}

/**
 * Theme provider for atomic design system that integrates with Material Theme
 * Wraps the entire app to provide design tokens to all atomic components
 */
@Composable
fun ProvideBassheadTheme(
    isDarkTheme: Boolean = false,
    colors: BassheadColors = if (isDarkTheme) BassheadDarkColors else BassheadLightColors,
    typography: BassheadTypography = BassheadTypographyTokens,
    spacing: BassheadSpacing = BassheadSpacingTokens,
    elevation: BassheadElevation = BassheadElevationTokens,
    content: @Composable () -> Unit,
) {
    // Create Material 3 ColorScheme from our BassheadColors
    val materialColorScheme = if (isDarkTheme) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            secondaryContainer = colors.secondaryContainer,
            onSecondaryContainer = colors.onSecondaryContainer,
            tertiary = colors.tertiary,
            onTertiary = colors.onTertiary,
            tertiaryContainer = colors.tertiaryContainer,
            onTertiaryContainer = colors.onTertiaryContainer,
            error = colors.error,
            onError = colors.onError,
            errorContainer = colors.errorContainer,
            onErrorContainer = colors.onErrorContainer,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surfaceVariant,
            onSurfaceVariant = colors.onSurfaceVariant,
            outline = colors.outline,
            outlineVariant = colors.outlineVariant,
            scrim = colors.scrim,
            inverseSurface = colors.inverseSurface,
            inverseOnSurface = colors.inverseOnSurface,
            inversePrimary = colors.inversePrimary,
            surfaceDim = colors.surfaceDim,
            surfaceBright = colors.surfaceBright,
            surfaceContainerLowest = colors.surfaceContainerLowest,
            surfaceContainerLow = colors.surfaceContainerLow,
            surfaceContainer = colors.surfaceContainer,
            surfaceContainerHigh = colors.surfaceContainerHigh,
            surfaceContainerHighest = colors.surfaceContainerHighest,
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = colors.onPrimary,
            primaryContainer = colors.primaryContainer,
            onPrimaryContainer = colors.onPrimaryContainer,
            secondary = colors.secondary,
            onSecondary = colors.onSecondary,
            secondaryContainer = colors.secondaryContainer,
            onSecondaryContainer = colors.onSecondaryContainer,
            tertiary = colors.tertiary,
            onTertiary = colors.onTertiary,
            tertiaryContainer = colors.tertiaryContainer,
            onTertiaryContainer = colors.onTertiaryContainer,
            error = colors.error,
            onError = colors.onError,
            errorContainer = colors.errorContainer,
            onErrorContainer = colors.onErrorContainer,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.surface,
            onSurface = colors.onSurface,
            surfaceVariant = colors.surfaceVariant,
            onSurfaceVariant = colors.onSurfaceVariant,
            outline = colors.outline,
            outlineVariant = colors.outlineVariant,
            scrim = colors.scrim,
            inverseSurface = colors.inverseSurface,
            inverseOnSurface = colors.inverseOnSurface,
            inversePrimary = colors.inversePrimary,
            surfaceDim = colors.surfaceDim,
            surfaceBright = colors.surfaceBright,
            surfaceContainerLowest = colors.surfaceContainerLowest,
            surfaceContainerLow = colors.surfaceContainerLow,
            surfaceContainer = colors.surfaceContainer,
            surfaceContainerHigh = colors.surfaceContainerHigh,
            surfaceContainerHighest = colors.surfaceContainerHighest,
        )
    }

    // Provide both our custom design tokens AND Material Theme integration
    CompositionLocalProvider(
        LocalBassheadColors provides colors,
        LocalBassheadTypography provides typography,
        LocalBassheadSpacing provides spacing,
        LocalBassheadElevation provides elevation,
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            content = content,
        )
    }
}

/**
 * Helper function for clear error messaging
 */
private fun noLocalProvidedFor(name: String): Nothing {
    error(
        "CompositionLocal $name not present. " +
            "Make sure your content is wrapped with ProvideBassheadTheme { ... } " +
            "to enable atomic design system components.",
    )
}
