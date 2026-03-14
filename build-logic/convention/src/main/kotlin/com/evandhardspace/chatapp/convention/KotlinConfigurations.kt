package com.evandhardspace.chatapp.convention

val kotlinFreeCompileArgs: List<String>
    get() = listOf(
        "-Xexpect-actual-classes",
        "-Xcontext-parameters",
        "-Xexplicit-backing-fields",
    )

val kotlinOptInAnnotations: List<String>
    get() = listOf(
        "kotlin.RequiresOptIn",
        "kotlin.time.ExperimentalTime",
        "kotlinx.coroutines.FlowPreview",
        "kotlinx.cinterop.ExperimentalForeignApi",
        "androidx.compose.material3.ExperimentalMaterial3Api",
        "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
        "androidx.compose.ui.ExperimentalComposeUiApi",
        "kotlin.uuid.ExperimentalUuidApi",
    )
