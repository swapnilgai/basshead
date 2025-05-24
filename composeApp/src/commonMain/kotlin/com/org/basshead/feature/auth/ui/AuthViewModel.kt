package com.org.basshead.feature.auth.ui

import com.org.basshead.utils.ui.BaseViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.launch

class AuthViewModel(val supabaseClient : SupabaseClient): BaseViewModel<Unit>(Unit) {

    init {

}
}