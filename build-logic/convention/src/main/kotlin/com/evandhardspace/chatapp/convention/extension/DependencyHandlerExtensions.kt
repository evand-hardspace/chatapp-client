package com.evandhardspace.chatapp.convention.extension

import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementation(
    dependencyNotation: Any,
) = add("implementation", dependencyNotation)

fun DependencyHandlerScope.testImplementation(
    dependencyNotation: Any,
) = add("testImplementation", dependencyNotation)

fun DependencyHandlerScope.coreLibraryDesugaring(
    dependencyNotation: Any,
) = add("coreLibraryDesugaring", dependencyNotation)

fun DependencyHandlerScope.debugImplementation(
    dependencyNotation: Any,
) = add("debugImplementation", dependencyNotation)
