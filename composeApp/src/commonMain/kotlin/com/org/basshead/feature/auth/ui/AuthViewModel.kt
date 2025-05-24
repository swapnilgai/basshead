package com.org.basshead.feature.auth.ui

import com.org.basshead.utils.ui.BaseViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

class AuthViewModel(val supabaseClient : SupabaseClient): BaseViewModel<Unit>(Unit) {

    init {
        baseViewModelScope.launch {
            supabaseClient.auth.sessionStatus.collect {
                when(it) {
                    is SessionStatus.Authenticated -> {
                        println("Received new authenticated session.")
                        when(it.source) { //Check the source of the session
                            SessionSource.External -> TODO()
                            is SessionSource.Refresh -> TODO()
                            is SessionSource.SignIn -> TODO()
                            is SessionSource.SignUp -> TODO()
                            SessionSource.Storage -> TODO()
                            SessionSource.Storage -> TODO()
                            SessionSource.Unknown -> TODO()
                            is SessionSource.UserChanged -> TODO()
                            is SessionSource.UserIdentitiesChanged -> TODO()
                            else -> {}
                        }
                    }
                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.RefreshFailure -> println("Refresh failure ${it.cause}") //Either a network error or a internal server error
                    is SessionStatus.NotAuthenticated -> {
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