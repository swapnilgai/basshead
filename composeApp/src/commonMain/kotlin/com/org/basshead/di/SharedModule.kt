package com.org.basshead.di

import com.org.basshead.feature.auth.interactor.AuthInteractor
import com.org.basshead.feature.auth.interactor.AuthInteractorImpl
import com.org.basshead.feature.auth.presentation.AuthViewModel
import com.org.basshead.feature.dashboard.interactor.DashBoardInteractor
import com.org.basshead.feature.dashboard.interactor.DashBoardInteractorImpl
import com.org.basshead.feature.dashboard.presentation.DashBoardViewModel
import com.org.basshead.feature.festivaldetail.interactor.FestivalCacheInteractor
import com.org.basshead.feature.festivaldetail.interactor.FestivalCacheInteractorImpl
import com.org.basshead.feature.festivaldetail.presentation.FestivalDetailViewModel
import com.org.basshead.feature.main.presentation.MainViewModel
import com.org.basshead.feature.profile.presentation.ProfileViewModel
import com.org.basshead.feature.search.interactor.SearchInteractor
import com.org.basshead.feature.search.interactor.SearchInteractorImpl
import com.org.basshead.feature.search.presentation.SearchViewModel
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
    single<FestivalCacheInteractor> { FestivalCacheInteractorImpl(get()) }
    viewModel { DashBoardViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { (festivalId: String) -> FestivalDetailViewModel(get(), get(), get(), festivalId) }
}

val mainModule = module {
    viewModel { MainViewModel() }
}

val searchModule = module {
    single<SearchInteractor> { SearchInteractorImpl(get()) }
    viewModel { SearchViewModel(get()) }
}
