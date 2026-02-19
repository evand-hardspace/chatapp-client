package com.evandhardspace.chat.presentation.di

import com.evandhardspace.chat.presentation.chat_list.ChatListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(::ChatListViewModel)
}
