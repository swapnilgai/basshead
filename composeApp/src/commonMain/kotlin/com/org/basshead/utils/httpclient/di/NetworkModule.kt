package com.org.basshead.utils.httpclient.di

import com.org.basshead.utils.httpclient.HttpClient
import io.github.jan.supabase.SupabaseClient
import org.koin.dsl.module

val httpClientModule = module {
    single<SupabaseClient> { HttpClient(get()).supabaseClient }
}
