package com.evandhardspace.chatapp.di

import com.evandhardspace.auth.presentation.di.AuthPresentationModule
import com.evandhardspace.chat.presentation.di.ChatPresentationModule
import com.evandhardspace.core.data.di.CoroutineModule
import com.evandhardspace.core.navigation.di.CoreNavigationModule
import com.evandhardspace.core.presentation.util.di.PresentationModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module
@ComponentScan("com.evandhardspace.chatapp")
class AppModule

@KoinApplication(
    modules = [
        AppModule::class,
        AuthPresentationModule::class,
        ChatPresentationModule::class,
        CoreNavigationModule::class,
        CoroutineModule::class,
        PresentationModule::class,
    ]
)
class ChatappKoinApp
