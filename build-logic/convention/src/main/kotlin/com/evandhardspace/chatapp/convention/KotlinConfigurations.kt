package com.evandhardspace.chatapp.convention

val kotlinFreeCompileArgs: List<String>
    get() = listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-opt-in=kotlin.time.ExperimentalTime",
        "-Xexpect-actual-classes",
        "-Xcontext-parameters",
        "-Xreturn-value-checker=full",
        "-Xexplicit-backing-fields",
    )
