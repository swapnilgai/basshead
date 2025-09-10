package com.org.basshead.feature.splash.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.splash_loading
import basshead.composeapp.generated.resources.splash_logo_description
import com.org.basshead.design.organisms.BassheadSplashScreenLayout
import com.org.basshead.feature.splash.presentation.SplashViewModel
import com.org.basshead.utils.ui.UiState
import org.koin.compose.viewmodel.koinViewModel
import com.org.basshead.utils.ui.Route as BaseRoute

@Composable
fun SplashScreenRoot(
    viewModel: SplashViewModel = koinViewModel(),
    navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state.value) {
        is UiState.Content -> {
            SplashScreen()
        }
        is UiState.Navigate -> {
            when (currentState.route) {
                is BaseRoute.InternalDirection -> {
                    navigate(currentState.route.destination, currentState.route.popUpTp, currentState.route.inclusive)
                }
                is BaseRoute.Back -> {
                }
            }
        }
        else -> {
            SplashScreen() //TODO handel loading and error states if needed
        }
    }
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    BassheadSplashScreenLayout(
        logoContentDescription = Res.string.splash_logo_description,
        loadingText = if (isLoading) Res.string.splash_loading else null,
        isLoading = isLoading,
        modifier = modifier,
    )
}
