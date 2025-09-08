package com.org.basshead.feature.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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
import com.org.basshead.design.atoms.BassheadOutlinedTextField
import com.org.basshead.design.atoms.BassheadTextButton
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.stringResource

/**
 * Flattened App Logo - Direct composition, no Column wrapper
 * Performance optimized: Single composable, minimal overhead
 */
@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Pets,
        contentDescription = stringResource(Res.string.app_name) + " logo",
        modifier = modifier.size(100.dp),
        tint = BassheadTheme.colors.onPrimary
    )
}

/**
 * Flattened App Name - Direct text composition
 */
@Composable
fun AppName(
    modifier: Modifier = Modifier
) {
    BassheadDisplaySmall(
        text = stringResource(Res.string.app_name),
        color = BassheadTheme.colors.onPrimary,
        modifier = modifier
    )
}

/**
 * Flattened App Tagline - Direct text composition
 */
@Composable
fun AppTagline(
    modifier: Modifier = Modifier
) {
    BassheadBodyLarge(
        text = stringResource(Res.string.tagline),
        color = BassheadTheme.colors.onPrimary.copy(alpha = 0.87f),
        modifier = modifier
    )
}

/**
 * Flattened Email Field - Direct text field, no Column wrapper
 * Performance optimized: Stable keyboard options, minimal recomposition
 */
@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val keyboardOptions = remember {
        KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    }

    BassheadOutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        label = stringResource(Res.string.email),
        leadingIcon = Icons.Default.Email
    )
}

/**
 * Flattened Password Field - Direct text field with isolated visibility state
 * Performance optimized: State scoped to this composable only
 */
@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    // Use rememberSaveable to survive configuration changes
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val keyboardOptions = remember {
        KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )
    }

    BassheadOutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        label = stringResource(Res.string.password),
        leadingIcon = Icons.Default.Lock,
        trailingIcon = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
        onTrailingIconClick = { passwordVisible = !passwordVisible }
    )
}

/**
 * Flattened Login Button - Direct button composition
 */
@Composable
fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    BassheadButton(
        text = stringResource(Res.string.login),
        onClick = onClick,
        enabled = enabled && !isLoading,
        isLoading = isLoading,
        modifier = modifier.fillMaxWidth()
    )
}

/**
 * Flattened Signup Prompt - Direct text composition
 */
@Composable
fun SignupPrompt(
    modifier: Modifier = Modifier
) {
    BassheadBodyMedium(
        text = "Don't have an account?",
        color = BassheadTheme.colors.onPrimary.copy(alpha = 0.87f),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

/**
 * Flattened Signup Button - Direct button composition
 */
@Composable
fun SignupButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    BassheadTextButton(
        text = stringResource(Res.string.create_account),
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
    )
}

// LEGACY COMPONENTS - Kept for backward compatibility but discouraged
// These create unnecessary Column wrappers and should be replaced with flattened components above

/**
 * @deprecated Use individual flattened components (AppLogo, AppName, AppTagline) instead
 * This creates unnecessary Column nesting and composition overhead
 */
@Deprecated("Use flattened components instead", ReplaceWith("Use AppLogo, AppName, AppTagline directly"))
@Composable
fun AppBrandingSection(
    modifier: Modifier = Modifier
) {
    // Kept for compatibility but adds unnecessary Column wrapper
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium),
        modifier = modifier
    ) {
        AppLogo()
        AppName()
        AppTagline()
    }
}

/**
 * @deprecated Use EmailField and PasswordField directly instead
 * This creates unnecessary Column nesting and composition overhead
 */
@Deprecated("Use flattened components instead", ReplaceWith("Use EmailField, PasswordField directly"))
@Composable
fun LoginCredentialsInput(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    // Kept for compatibility but adds unnecessary Column wrapper
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
    ) {
        EmailField(email, onEmailChange, enabled)
        PasswordField(password, onPasswordChange, enabled)
    }
}

/**
 * @deprecated Use LoginButton, SignupPrompt, SignupButton directly instead
 * This creates unnecessary Column nesting and composition overhead
 */
@Deprecated("Use flattened components instead", ReplaceWith("Use LoginButton, SignupPrompt, SignupButton directly"))
@Composable
fun LoginActionsPanel(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    // Kept for compatibility but adds unnecessary Column wrapper
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
    ) {
        LoginButton(onLoginClick, enabled, isLoading)
        SignupPrompt()
        SignupButton(onSignUpClick, enabled)
    }
}
