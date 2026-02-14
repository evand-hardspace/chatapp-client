package com.evandhardspace.chatapp

import android.app.Application
import com.evandhardspace.chatapp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ChatApplication)
            androidLogger()
        }
    }
}
