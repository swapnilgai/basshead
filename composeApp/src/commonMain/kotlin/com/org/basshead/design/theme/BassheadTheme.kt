package com.org.basshead.design.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
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
import com.org.basshead.design.tokens.toColorScheme

/**
 * CompositionLocal providers for design tokens
 * Following Reddit's approach with staticCompositionLocalOf for performance
 */
val LocalBassheadColors = staticCompositionLocalOf<BassheadColors> {
    noLocalProvidedFor("LocalBassheadColors")
}

val LocalBassheadTypography = compositionLocalOf<BassheadTypography> {
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
 *
 * Why MaterialTheme integration:
 * 1. Ensures compatibility with Material 3 components (TextField, Button, etc.)
 * 2. Provides seamless theme inheritance for third-party components
 * 3. Maintains accessibility standards across the app
 * 4. Enables proper theme animations and transitions
 *
 * @param colors Custom colors override
 * @param typography Custom typography override
 * @param spacing Custom spacing override
 * @param elevation Custom elevation override
 * @param content Content to be themed
 */
@Composable
fun ProvideBassheadTheme(
    isDarkTheme: Boolean = false,
    typography: BassheadTypography = BassheadTypographyTokens,
    spacing: BassheadSpacing = BassheadSpacingTokens,
    elevation: BassheadElevation = BassheadElevationTokens,
    content: @Composable () -> Unit,
) {
    // Use light theme by default, dark theme logic will be added later
    val themeColors = if (isDarkTheme) BassheadDarkColors else BassheadLightColors

    // Create Material 3 ColorScheme using our BassheadColors factory methods
    val materialColorScheme = remember(themeColors, isDarkTheme) {
        themeColors.toColorScheme(isDark = isDarkTheme)
    }

    // Provide both our custom design tokens AND Material Theme integration
    // This approach ensures full reactivity to theme changes
    CompositionLocalProvider(
        LocalBassheadColors provides themeColors,
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
