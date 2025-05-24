package com.org.basshead.feature.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.compose_multiplatform
import com.org.basshead.feature.splash.SplashViewModel
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
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center) {
        Column {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.padding(20.dp))
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}