package com.org.basshead.feature.splash.presentation

import com.org.basshead.navigation.Route
import com.org.basshead.utils.ui.BaseViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(val supabaseClient: SupabaseClient): BaseViewModel<Unit>(Unit) {

    init {
        checkAuthAndLoadData()
    }

    private fun checkAuthAndLoadData(){
        baseViewModelScope.launch {
            supabaseClient.auth.sessionStatus.collect {
                setLoading()
                when(it) {
                    is SessionStatus.Authenticated -> {
                        navigate(destination = Route.Auth::class.simpleName!!, popUpTp = Route.Splash::class.simpleName)
                    }
                    SessionStatus.Initializing -> println("Initializing")
                    is SessionStatus.RefreshFailure -> println("Refresh failure ${it.cause}") //Either a network error or a internal server error
                    is SessionStatus.NotAuthenticated -> {
                        delay(3000)
                        navigate(
                            destination = Route.Auth::class.simpleName!!
                        )
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