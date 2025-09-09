package com.org.basshead.feature.auth.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.app_name
import basshead.composeapp.generated.resources.create_account
import basshead.composeapp.generated.resources.email
import basshead.composeapp.generated.resources.login
import basshead.composeapp.generated.resources.password
import basshead.composeapp.generated.resources.signup_prompt
import basshead.composeapp.generated.resources.tagline
import com.org.basshead.design.molecules.BassheadAppLogo
import com.org.basshead.design.molecules.BassheadAppName
import com.org.basshead.design.molecules.BassheadAppTagline
import com.org.basshead.design.molecules.BassheadEmailField
import com.org.basshead.design.molecules.BassheadLoginButton
import com.org.basshead.design.molecules.BassheadPasswordField
import com.org.basshead.design.molecules.BassheadSignupPrompt
import com.org.basshead.design.theme.BassheadTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppLogo(
    modifier: Modifier = Modifier
) {
    BassheadAppLogo(
        modifier = modifier,
        size = 100.dp,
        tint = BassheadTheme.colors.onPrimary,
        contentDescription = stringResource(Res.string.app_name) + " logo"
    )
}

@Composable
fun AppName(
    modifier: Modifier = Modifier
) {
    BassheadAppName(
        modifier = modifier,
        text = stringResource(Res.string.app_name),
        color = BassheadTheme.colors.onPrimary
    )
}

@Composable
fun AppTagline(
    modifier: Modifier = Modifier
) {
    BassheadAppTagline(
        modifier = modifier,
        text = stringResource(Res.string.tagline),
        color = BassheadTheme.colors.onPrimary.copy(alpha = 0.87f)
    )
}

@Composable
fun EmailField(
    email: String,
    onEmailChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    BassheadEmailField(
        email = email,
        onEmailChange = onEmailChange,
        enabled = enabled,
        label = stringResource(Res.string.email),
        modifier = modifier
    )
}

@Composable
fun PasswordField(
    password: String,
    onPasswordChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    BassheadPasswordField(
        password = password,
        onPasswordChange = onPasswordChange,
        enabled = enabled,
        label = stringResource(Res.string.password),
        modifier = modifier
    )
}

@Composable
fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    BassheadLoginButton(
        onClick = onClick,
        enabled = enabled,
        isLoading = isLoading,
        text = stringResource(Res.string.login),
        modifier = modifier
    )
}

@Composable
fun SignupSection(
    onSignUpClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    BassheadSignupPrompt(
        onSignupClick = onSignUpClick,
        enabled = enabled,
        promptText = stringResource(Res.string.signup_prompt),
        buttonText = stringResource(Res.string.create_account),
        textColor = BassheadTheme.colors.onPrimary.copy(alpha = 0.87f),
        modifier = modifier
    )
}
