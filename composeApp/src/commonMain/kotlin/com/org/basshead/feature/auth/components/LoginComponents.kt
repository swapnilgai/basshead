package com.org.basshead.feature.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.app_name
import basshead.composeapp.generated.resources.create_account
import basshead.composeapp.generated.resources.email
import basshead.composeapp.generated.resources.login
import basshead.composeapp.generated.resources.password
import basshead.composeapp.generated.resources.tagline
import com.org.basshead.design.atoms.BassheadBodyLarge
import com.org.basshead.design.atoms.BassheadBodyMedium
import com.org.basshead.design.atoms.BassheadButton
import com.org.basshead.design.atoms.BassheadDisplaySmall
import com.org.basshead.design.atoms.BassheadIcon
import com.org.basshead.design.atoms.BassheadOutlinedTextField
import com.org.basshead.design.atoms.BassheadTextButton
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.stringResource

/**
 * App Branding Header - Displays app branding (logo, name, tagline)
 * Performance optimized: Flattened structure, no nested Column
 */
@Composable
fun AppBrandingHeader(
    modifier: Modifier = Modifier
) {
    // Remove the nested Column - just return the content directly
    // The parent Column in CompleteLoginForm will handle the arrangement
    BassheadIcon(
        imageVector = Icons.Default.Pets,
        contentDescription = stringResource(Res.string.app_name) + " logo",
        size = 100.dp,
        tint = BassheadTheme.colors.onPrimary,
        modifier = modifier
    )
}

/**
 * App Name Display - Separate component for better performance isolation
 */
@Composable
fun AppNameDisplay(
    modifier: Modifier = Modifier
) {
    BassheadDisplaySmall(
        text = stringResource(Res.string.app_name),
        color = BassheadTheme.colors.onPrimary,
        modifier = modifier
    )
}

/**
 * App Tagline Display - Separate component for better performance isolation
 */
@Composable
fun AppTaglineDisplay(
    modifier: Modifier = Modifier
) {
    BassheadBodyLarge(
        text = stringResource(Res.string.tagline),
        color = BassheadTheme.colors.onPrimary.copy(alpha = 0.87f),
        modifier = modifier
    )
}

/**
 * Login Credentials Input - Email and password input fields
 * Performance optimized: Only recomposes when email/password changes
 */
@Composable
fun LoginCredentialsInput(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.semantics {
            contentDescription = "Login form with email and password fields"
        },
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
    ) {
        // Email field - optimized with remember for keyboard options
        val emailKeyboardOptions = remember {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        }

        BassheadOutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(Res.string.email),
            leadingIcon = Icons.Default.Email,
            keyboardOptions = emailKeyboardOptions,
            singleLine = true,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
        )

        // Password field with visibility toggle - state isolated to this component
        var passwordVisible by remember { mutableStateOf(false) }
        val passwordKeyboardOptions = remember {
            KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        }

        BassheadOutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(Res.string.password),
            leadingIcon = Icons.Default.Lock,
            trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
            onTrailingIconClick = { passwordVisible = !passwordVisible },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = passwordKeyboardOptions,
            singleLine = true,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Login Actions Panel - Primary and secondary action buttons
 * Performance optimized: Only recomposes when enabled/loading state changes
 */
@Composable
fun LoginActionsPanel(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
    ) {
        // Primary button
        BassheadButton(
            text = stringResource(Res.string.login),
            onClick = onLoginClick,
            enabled = enabled && !isLoading,
            isLoading = isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        // Secondary action section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.small)
        ) {
            BassheadBodyMedium(
                text = "Don't have an account?",
                color = BassheadTheme.colors.onPrimary.copy(alpha = 0.87f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            BassheadTextButton(
                text = stringResource(Res.string.create_account),
                onClick = onSignUpClick,
                enabled = enabled && !isLoading
            )
        }
    }
}

/**
 * Complete Login Form - Combines all login-related components
 * Performance optimized: Flattened structure eliminates nested Columns and overdraw
 */
@Composable
fun CompleteLoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Column(
        modifier = modifier.semantics {
            contentDescription = "Complete login form with branding and authentication fields"
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
    ) {
        // Flattened branding components - no nested Column, better performance
        AppBrandingHeader()

        AppNameDisplay()

        AppTaglineDisplay()

        // Add extra spacing after branding
        Spacer(
            modifier = Modifier.height(BassheadTheme.spacing.large)
        )

        // Credentials input - only recomposes when email/password changes
        LoginCredentialsInput(
            email = email,
            password = password,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            enabled = enabled
        )

        // Add spacing before actions
        Spacer(
            modifier = Modifier.height(BassheadTheme.spacing.medium)
        )

        // Actions panel - only recomposes when enabled/loading changes
        LoginActionsPanel(
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
            enabled = enabled,
            isLoading = isLoading
        )
    }
}
