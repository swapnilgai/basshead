package com.org.basshead

import android.app.Application
import com.org.basshead.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MainApplication)
        }
    }
}