package com.org.basshead.design.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.org.basshead.design.molecules.BassheadSplashContent
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.StringResource

/**
 * Splash screen organism - complete splash screen layout with theming
 */

/**
 * Complete splash screen organism with gradient background and centered content
 * Optimized hierarchy to reduce overdraw
 */
@Composable
fun BassheadSplashScreenLayout(
    logoContentDescription: StringResource,
    loadingText: StringResource? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BassheadTheme.colors.primaryContainer,
                        BassheadTheme.colors.primary,
                    ),
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        BassheadSplashContent(
            logoContentDescription = logoContentDescription,
            loadingText = loadingText,
            isLoading = isLoading,
        )
    }
}
