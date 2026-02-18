package com.evandhardspace.chatapp.di

import com.evandhardspace.chatapp.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
}