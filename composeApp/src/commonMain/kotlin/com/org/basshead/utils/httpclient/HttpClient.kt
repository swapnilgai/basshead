package com.org.basshead.utils.httpclient

import com.org.basshead.utils.core.ProjectEnvironment
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class HttpClient(projectEnvironment: ProjectEnvironment) {

    val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = projectEnvironment.url,
        supabaseKey = projectEnvironment.apiKey,
    ) {
        install(Postgrest)
        install(Auth)
    }
}
