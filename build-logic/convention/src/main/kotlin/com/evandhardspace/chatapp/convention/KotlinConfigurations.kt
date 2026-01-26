package com.evandhardspace.chatapp.convention

val kotlinFreeCompileArgs: List<String>
    get() = listOf(
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
    )