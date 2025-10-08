package com.org.basshead.design.molecules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.dog
import com.org.basshead.design.atoms.BassheadLoadingIndicator
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Splash screen specific molecules following atomic design pattern
 */

/**
 * Splash logo molecule - displays the main app logo with proper sizing and alignment
 */
@Composable
fun BassheadSplashLogo(
    contentDescription: StringResource,
    modifier: Modifier = Modifier,
    logoSize: androidx.compose.ui.unit.Dp = 200.dp,
) {
    Image(
        painter = painterResource(Res.drawable.dog),
        contentDescription = stringResource(contentDescription),
        modifier = modifier.size(logoSize),
    )
}

/**
 * Splash loading section molecule - combines logo with optional loading indicator
 * Flattened hierarchy to reduce overdraw
 */
@Composable
fun BassheadSplashContent(
    logoContentDescription: StringResource,
    loadingText: StringResource? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BassheadSplashLogo(
            contentDescription = logoContentDescription,
        )

        if (isLoading || loadingText != null) {
            Spacer(modifier = Modifier.height(BassheadTheme.spacing.large))

            if (isLoading) {
                BassheadLoadingIndicator(
                    color = BassheadTheme.colors.primary, // Changed from onPrimary for better contrast
                )
            }

            loadingText?.let { text ->
                Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium))
                Text(
                    text = stringResource(text),
                    style = BassheadTheme.typography.bodyMedium,
                    color = BassheadTheme.colors.onBackground, // Changed from onPrimary for proper contrast
                )
            }
        }
    }
}
