package com.org.basshead.feature.auth.ui

import com.org.basshead.feature.auth.interactor.AuthInteractor
import com.org.basshead.navigation.Route
import com.org.basshead.utils.ui.BaseViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface AuthActions{
    data class onLoginClicked(val email: String, val password: String): AuthActions
    data class onSignUpClicked(val email: String, val password: String): AuthActions
}

class AuthViewModel(val supabaseClient : SupabaseClient, val authInteractor: AuthInteractor): BaseViewModel<Unit>(Unit) {

    init {
        checkAuthAndLoadData()
    }

    fun onAction(authAction: AuthActions){
        when(val action = authAction) {
            is AuthActions.onLoginClicked -> onLogInClicked(email = action.email, password = action.password)
            is AuthActions.onSignUpClicked -> onSignUpClicked(email = action.email, password = action.password)
            else -> {}
        }
    }

    private fun onLogInClicked(email: String, password: String){
        baseViewModelScope.launch {
            setLoading()
            authInteractor.logIn(email, password)
            setContent(getContent())
        }
    }

    private fun onSignUpClicked(email: String, password: String){
        baseViewModelScope.launch {
            setLoading()
            authInteractor.signUp(email, password)
            setContent(getContent())
        }
    }

    private fun checkAuthAndLoadData(){
        baseViewModelScope.launch {
            supabaseClient.auth.sessionStatus.collect {
                setLoading()
                when(it) {
                    is SessionStatus.Authenticated -> {
                        println("Initializing")
                        //navigate(destination = Route.Auth::class.simpleName!!, popUpTp = Route.Splash::class.simpleName)
                    }
                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.RefreshFailure -> println("Refresh failure ${it.cause}") //Either a network error or a internal server error
                    is SessionStatus.NotAuthenticated -> {
                        setContent(getContent())
                        if(it.isSignOut) {
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