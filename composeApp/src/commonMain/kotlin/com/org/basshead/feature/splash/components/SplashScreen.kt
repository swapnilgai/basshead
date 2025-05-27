package com.org.basshead.feature.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.dog
import com.org.basshead.feature.splash.presentation.SplashViewModel
import com.org.basshead.utils.core.LightOrange
import com.org.basshead.utils.core.PrimaryOrange
import com.org.basshead.utils.ui.Route
import com.org.basshead.utils.ui.UiState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreenRoot(viewModel: SplashViewModel = koinViewModel(),
                     navigate: (destination: String, popUpTp: String?, inclusive: Boolean?) -> Unit){

    val state = viewModel.state.collectAsStateWithLifecycle()

    when(val currentState = state.value) {
        is UiState.Content -> { SplashScreen() }
        is UiState.Navigate -> {
            when(currentState.route) {
                is Route.InternalDirection -> navigate(currentState.route.destination, currentState.route.popUpTp, currentState.route.inclusive)
                is Route.Back -> {}
            }
        }
        else -> { }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LightOrange,
                        PrimaryOrange
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.dog),
            contentDescription = "Headbanging Dog",
            modifier = Modifier
                .size(200.dp)
        )
    }
}


