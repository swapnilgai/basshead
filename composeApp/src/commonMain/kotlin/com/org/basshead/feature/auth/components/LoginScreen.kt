package com.org.basshead.feature.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import basshead.composeapp.generated.resources.Res
import basshead.composeapp.generated.resources.app_name
import basshead.composeapp.generated.resources.create_account
import basshead.composeapp.generated.resources.dog
import basshead.composeapp.generated.resources.email
import basshead.composeapp.generated.resources.login
import basshead.composeapp.generated.resources.password
import basshead.composeapp.generated.resources.rating
import basshead.composeapp.generated.resources.tagline
import com.org.basshead.feature.auth.ui.AuthActions
import com.org.basshead.feature.auth.ui.AuthViewModel
import com.org.basshead.utils.components.ErrorScreen
import com.org.basshead.utils.components.LoadingScreen
import com.org.basshead.utils.core.DesertWhite
import com.org.basshead.utils.core.LightOrange
import com.org.basshead.utils.core.PrimaryOrange
import com.org.basshead.utils.ui.UiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun LoginScreenRoot(viewModel: AuthViewModel = koinViewModel()){
    val state = viewModel.state.collectAsStateWithLifecycle()
    var showError by remember { mutableStateOf(true) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val onLoginClicked = remember {
        { viewModel.onAction(AuthActions.onLoginClicked(email, password)) }
    }

    val onSignUpClicked = remember {
        {
            viewModel.onAction(AuthActions.onSignUpClicked(email, password))
        }
    }

    LaunchedEffect(state.value){
        if(state.value is UiState.Error)
            showError = true
    }

    when (val currentState = state.value) {
        is UiState.Content -> {
            LoginScreen(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLogInClicked = onLoginClicked,
                onSignUpClicked = onSignUpClicked
            )

            if(currentState.isLoadingUi) LoadingScreen()
        }

        is UiState.Error -> {
            LoginScreen(
                email = email,
                password = password,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onLogInClicked = onLoginClicked,
                onSignUpClicked = onSignUpClicked
            )

            if (showError) {
                ErrorScreen(currentState.message.asString()) { showError = false }
            }
        }

        else -> {}
    }
}

@Composable
fun LoginScreen(  email: String,
                  password: String,
                  onEmailChange: (String) -> Unit,
                  onPasswordChange: (String) -> Unit,
                  onLogInClicked: () -> Unit,
                  onSignUpClicked: () -> Unit){

    val scrollState = rememberScrollState()

    Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(scrollState)
                    .background( brush = Brush.verticalGradient(
                        colors = listOf(
                            LightOrange,
                            PrimaryOrange
                        )
                    )
                ).imePadding()
            ) {
        Spacer(modifier = Modifier.height(128.dp))
        Column(modifier = Modifier.padding(24.dp)) {
            LoginHeader(
                email = email,
                password = password,
                onEmailChange = onEmailChange,
                onPasswordChanged = onPasswordChange
            )
            Spacer(modifier = Modifier.height(32.dp))
            LoginFooter(onLogInClicked = onLogInClicked, onSignUpClicked = onSignUpClicked)
        }
    }
}

@Composable
fun LoginHeader(email: String, password: String, onEmailChange: (String) -> Unit, onPasswordChanged: (String) -> Unit){

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(Res.drawable.dog), contentDescription = "",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = stringResource(Res.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = stringResource(Res.string.tagline),
            fontWeight = FontWeight.Light,
            color = Color.White,
            fontSize = 16.sp
        )
        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text(stringResource(Res.string.email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { onPasswordChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.password)) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions =  KeyboardOptions.Default.copy( keyboardType =  KeyboardType.Password)
        )
    }
}

@Composable
fun LoginFooter(onLogInClicked: () -> Unit, onSignUpClicked: () -> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onLogInClicked() },
            colors = ButtonDefaults.buttonColors(PrimaryOrange)
        ){
            Text(stringResource(Res.string.login))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSignUpClicked() },
            colors = ButtonDefaults.buttonColors(DesertWhite)
        ){
            Text(stringResource(Res.string.create_account))
        }
    }
}