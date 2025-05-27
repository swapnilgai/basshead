package com.org.basshead.di

import com.org.basshead.feature.auth.interactor.AuthInteractor
import com.org.basshead.feature.auth.interactor.AuthInteractorImpl
import com.org.basshead.feature.auth.presentation.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import com.org.basshead.utils.httpclient.HttpClient
import io.github.jan.supabase.SupabaseClient
import com.org.basshead.feature.splash.presentation.SplashViewModel


val authModule = module {
    single<AuthInteractor> { AuthInteractorImpl(get()) }
    viewModel { AuthViewModel(get(), get()) }
}

val httpClientModule = module {
    single<SupabaseClient> { HttpClient(get()).supabaseClient }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}