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
            else -> {}
        }
    }

    private fun onLogInClicked(email: String, password: String) {
        baseViewModelScope.launch {
            setLoading()
            authInteractor.logIn(email, password)
            setContent(getContent())
//            navigate(destination = Route.Dashboard::class.simpleName!!,
//                popUpTp = Route.Splash::class.simpleName, inclusive = true)
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
            supabaseClient.auth.sessionStatus.collect {
                setLoading()
                when (it) {
                    is SessionStatus.Authenticated -> {
                        println("Initializing")
                        navigate(
                            destination = Route.Dashboard::class.simpleName!!,
                            popUpTp = Route.Splash::class.simpleName,
                            inclusive = true,
                        )
                    }
                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.RefreshFailure -> println("Refresh failure ${it.cause}") // Either a network error or a internal server error
                    is SessionStatus.NotAuthenticated -> {
                        setContent(getContent())
                        if (it.isSignOut) {
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
