package com.org.basshead.design.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadTextButton
import com.org.basshead.design.theme.BassheadTheme

/**
 * Molecular button components
 * Complex button combinations with specific behaviors and styling
 */

/**
 * Login button molecule - specialized button for login actions
 * Performance optimized: Consistent styling and loading states
 */
@Composable
fun BassheadLoginButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    text: String = "Log In"
) {
    BassheadButton(
        text = text,
        onClick = onClick,
        enabled = enabled && !isLoading,
        isLoading = isLoading,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Signup prompt molecule - text prompt with call-to-action button
 * Performance optimized: Consistent spacing and styling
 */
@Composable
fun BassheadSignupPrompt(
    onSignupClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    promptText: String = "Don't have an account?",
    buttonText: String = "Sign Up",
    textColor: androidx.compose.ui.graphics.Color = BassheadTheme.colors.onSurface.copy(alpha = 0.87f)
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small)
    ) {
        BassheadBodyMedium(
            text = promptText,
            color = textColor,
            textAlign = TextAlign.Center
        )

        BassheadTextButton(
            text = buttonText,
            onClick = onSignupClick,
            enabled = enabled
        )
    }
}

/**
 * Primary action button molecule - main call-to-action button
 * Performance optimized: Consistent primary button styling
 */
@Composable
fun BassheadPrimaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    BassheadButton(
        text = text,
        onClick = onClick,
        enabled = enabled && !isLoading,
        isLoading = isLoading,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Secondary action button molecule - secondary call-to-action button
 * Performance optimized: Consistent secondary button styling
 */
@Composable
fun BassheadSecondaryActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    BassheadTextButton(
        text = text,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    )
}
