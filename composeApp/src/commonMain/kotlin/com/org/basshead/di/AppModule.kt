package com.org.basshead.di

import com.org.basshead.BuildKonfig
import com.org.basshead.utils.core.DevelopmentEnvironment
import com.org.basshead.utils.core.ProductionEnvironment
import com.org.basshead.utils.core.ProjectEnvironment
import org.koin.dsl.module

val appModule = module {
    single<ProjectEnvironment> {
        if(BuildKonfig.environment == "dev")
            DevelopmentEnvironment()
        else
            ProductionEnvironment()
    }
}