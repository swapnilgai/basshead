package com.org.basshead.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun InitKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, httpClientModule, authModule, splashModule, dashboardModule, searchModule, mainModule, avatarModule)
    }
}
