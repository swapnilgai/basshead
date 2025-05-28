package com.org.basshead.di

import com.org.basshead.feature.auth.interactor.AuthInteractor
import com.org.basshead.feature.auth.interactor.AuthInteractorImpl
import com.org.basshead.feature.auth.presentation.AuthViewModel
import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.interactor.DashBoardInteractorImpl
import com.org.basshead.feature.dashboard.presentation.DashBoardViewModel
import com.org.basshead.feature.splash.presentation.SplashViewModel
import com.org.basshead.utils.httpclient.HttpClient
import io.github.jan.supabase.SupabaseClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

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

val dashboardModule = module {
    single<DashBoardInteractor> { DashBoardInteractorImpl(get()) }
    viewModel { DashBoardViewModel(get()) }
}
