package com.org.basshead.feature.auth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.org.basshead.design.theme.BassheadTheme
import com.org.basshead.feature.auth.presentation.AuthActions
import com.org.basshead.feature.auth.presentation.AuthViewModel
import com.org.basshead.utils.components.ErrorScreen
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel
import com.org.basshead.utils.ui.Route as BaseRoute

@Composable
fun LoginScreenRoot(
    viewModel: AuthViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Performance optimization: Use stable references to prevent recomposition
    val onLoginClicked = remember(email, password) {
        {
            viewModel.onAction(AuthActions.OnLoginClicked(email, password))
        }
    }

    val onSignUpClicked = remember(email, password) {
        {
            viewModel.onAction(AuthActions.OnSignUpClicked(email, password))
        }
    }

    LaunchedEffect(state.value) {
        if (state.value is UiState.Error) {
            showError = true
        }
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            LoginScreen(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLogInClicked = onLoginClicked,
                onSignUpClicked = onSignUpClicked,
                isLoading = currentState.isLoadingUi
            )

            if (currentState.isLoadingUi) LoadingScreen()
        }

        is UiState.Error -> {
            LoginScreen(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLogInClicked = onLoginClicked,
                onSignUpClicked = onSignUpClicked,
                isLoading = false
            )

            if (showError) {
                ErrorScreen(currentState.message.asString()) { showError = false }
            }
        }
        is UiState.Navigate -> {
            when (currentState.route) {
                is BaseRoute.InternalDirection -> {
                    navigate(currentState.route.destination, currentState.route.popUpTp, currentState.route.inclusive)
                }
                is BaseRoute.Back -> {
                    navigate("back", null, null)
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    isLoading: Boolean = false,
) {
    val scrollState = rememberScrollState()

    // Single background layer - no overdraw from multiple backgrounds
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BassheadTheme.colors.primary,
                        BassheadTheme.colors.primaryContainer,
                    ),
                ),
            )
            .imePadding()
            .semantics {
                contentDescription = "Login screen with authentication form"
            }
    ) {
        // FLATTENED SINGLE COLUMN - Direct component placement, no nesting
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = BassheadTheme.spacing.screenPadding),
            verticalArrangement = Arrangement.spacedBy(BassheadTheme.spacing.medium)
        ) {
            // Top spacer for visual balance
            Spacer(modifier = Modifier.height(60.dp))

            // FLATTENED BRANDING - Direct placement, no Column wrapper
            AppLogo(
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            AppName(
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            AppTagline(
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            // Extra spacing after branding
            Spacer(modifier = Modifier.height(BassheadTheme.spacing.large))

            // FLATTENED FORM FIELDS - Direct placement, no Column wrapper
            EmailField(
                email = email,
                onEmailChange = onEmailChange,
                enabled = !isLoading,
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            PasswordField(
                password = password,
                onPasswordChange = onPasswordChange,
                enabled = !isLoading,
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            // Spacing before actions
            Spacer(modifier = Modifier.height(BassheadTheme.spacing.medium))

            // FLATTENED ACTIONS - Direct placement, no Column wrapper
            LoginButton(
                onClick = onLogInClicked,
                enabled = !isLoading,
                isLoading = isLoading,
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            SignupSection(
                onSignUpClick = onSignUpClicked,
                enabled = !isLoading,
                modifier = Modifier.padding(horizontal = BassheadTheme.spacing.cardPadding)
            )

            // Bottom spacer for visual balance
            Spacer(modifier = Modifier.height(BassheadTheme.spacing.extraLarge))
        }
    }
}
