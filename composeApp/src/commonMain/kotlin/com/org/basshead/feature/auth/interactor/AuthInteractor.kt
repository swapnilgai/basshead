package com.org.basshead.feature.auth.interactor

import com.org.basshead.utils.interactor.Interactor
import com.org.basshead.utils.interactor.RetryOption
import com.org.basshead.utils.interactor.withInteractorContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo

interface AuthInteractor : Interactor {
    suspend fun signUp(email: String, password: String): UserInfo?
    suspend fun logIn(email: String, password: String)
    suspend fun getCurrentUser()
}

class AuthInteractorImpl(val supabaseClient: SupabaseClient) : AuthInteractor {

    override suspend fun signUp(email: String, password: String): UserInfo? {
        return withInteractorContext(retryOption = RetryOption(retryCount = 0)) {
            supabaseClient.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }

    override suspend fun logIn(email: String, password: String) {
        withInteractorContext(retryOption = RetryOption(retryCount = 2)) {
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }

    override suspend fun getCurrentUser() {
        withInteractorContext(retryOption = RetryOption(retryCount = 2)) {
            supabaseClient.auth.currentUserOrNull()
        }
    }
}
