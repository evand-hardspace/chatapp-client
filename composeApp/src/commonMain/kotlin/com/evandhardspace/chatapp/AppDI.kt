package com.evandhardspace.chatapp

import com.evandhardspace.auth.presentation.di.AuthPresentationModule
import com.evandhardspace.chat.presentation.di.ChatPresentationModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.KoinApplication
import org.koin.core.annotation.Module

@Module
@ComponentScan
class AppModule

@KoinApplication(
    modules = [
        AppModule::class,
        AuthPresentationModule::class,
        ChatPresentationModule::class,
    ]
)
class ChatappKoinApp
