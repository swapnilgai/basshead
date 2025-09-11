package com.org.basshead.feature.auth.presentation

import com.org.basshead.feature.auth.interactor.AuthInteractor
import com.org.basshead.navigation.Route
import com.org.basshead.utils.ui.BaseViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

sealed interface AuthActions {
    data class OnLoginClicked(val email: String, val password: String) : AuthActions
    data class OnSignUpClicked(val email: String, val password: String) : AuthActions
}

class AuthViewModel(val supabaseClient: SupabaseClient, val authInteractor: AuthInteractor) : BaseViewModel<Unit>(Unit) {

    init {
        checkAuthAndLoadData()
    }

    fun onAction(authAction: AuthActions) {
        when (val action = authAction) {
            is AuthActions.OnLoginClicked -> onLogInClicked(email = action.email, password = action.password)
            is AuthActions.OnSignUpClicked -> onSignUpClicked(email = action.email, password = action.password)
        }
    }

    private fun onLogInClicked(email: String, password: String) {
        baseViewModelScope.launch {
            setLoading()
            authInteractor.logIn(email, password)
            setContent(getContent())
        }
    }

    private fun onSignUpClicked(email: String, password: String) {
        baseViewModelScope.launch {
            setLoading()
            authInteractor.signUp(email, password)
            setContent(getContent())
        }
    }

    private fun checkAuthAndLoadData() {
        baseViewModelScope.launch {
            supabaseClient.auth.sessionStatus.collect { sessionStatus ->
                when (sessionStatus) {
                    is SessionStatus.Authenticated -> {
                        // Stop further processing and navigate immediately
                        navigate(
                            destination = Route.Dashboard::class.simpleName!!,
                            popUpTp = Route.Auth::class.simpleName,
                            inclusive = true,
                        )
                        return@collect // Exit the collection to prevent further state updates
                    }
                    SessionStatus.Initializing -> {
                        setLoading()
                        println("Initializing")
                    }
                    is SessionStatus.RefreshFailure -> {
                        println("Refresh failure ${sessionStatus.cause}")
                        setContent(getContent()) // Set content state for refresh failures
                    }
                    is SessionStatus.NotAuthenticated -> {
                        setContent(getContent())
                        if (sessionStatus.isSignOut) {
                            println("User signed out")
                        } else {
                            println("User not signed in")
                        }
                    }
                }
            }
        }
    }
}
