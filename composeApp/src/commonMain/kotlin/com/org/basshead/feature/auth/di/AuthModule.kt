package com.org.basshead.feature.auth.di

import com.org.basshead.feature.auth.interactor.AuthInteractor
import com.org.basshead.feature.auth.interactor.AuthInteractorImpl
import com.org.basshead.feature.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthInteractor> { AuthInteractorImpl(get()) }
    viewModel { AuthViewModel(get(), get()) }
}