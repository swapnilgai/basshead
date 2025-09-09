package com.org.basshead.design.theme

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
 * Immutable theme configuration for atomic design system
 * Ensures consistency across all atomic components
 */
@Immutable
data class BassheadThemeValues(
    val colors: BassheadColors,
    val typography: BassheadTypography,
    val spacing: BassheadSpacing,
    val elevation: BassheadElevation,
)

/**
 * Theme provider for atomic design system
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
    val themeValues = BassheadThemeValues(
        colors = colors,
        typography = typography,
        spacing = spacing,
        elevation = elevation,
    )

    CompositionLocalProvider(
        LocalBassheadColors provides themeValues.colors,
        LocalBassheadTypography provides themeValues.typography,
        LocalBassheadSpacing provides themeValues.spacing,
        LocalBassheadElevation provides themeValues.elevation,
        content = content,
    )
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
 * Helper function for clear error messaging
 */
private fun noLocalProvidedFor(name: String): Nothing {
    error(
        "CompositionLocal $name not present. " +
            "Make sure your content is wrapped with ProvideBassheadTheme { ... } " +
            "to enable atomic design system components.",
    )
}
