package com.org.basshead.feature.splash.di

import com.org.basshead.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}