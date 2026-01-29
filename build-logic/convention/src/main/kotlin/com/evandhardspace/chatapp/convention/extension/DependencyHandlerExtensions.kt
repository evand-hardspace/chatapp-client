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

fun DependencyHandlerScope.commonMainImplementation(
    dependencyNotation: Any,
) = add("commonMainImplementation", dependencyNotation)

fun DependencyHandlerScope.commonTestImplementation(
    dependencyNotation: Any,
) = add("commonTestImplementation", dependencyNotation)

fun DependencyHandlerScope.androidMainImplementation(
    dependencyNotation: Any,
) = add("androidMainImplementation", dependencyNotation)

fun DependencyHandlerScope.commonMainApi(
    dependencyNotation: Any,
) = add("commonMainApi", dependencyNotation)

fun DependencyHandlerScope.kspAndroid(
    dependencyNotation: Any,
) = add("kspAndroid", dependencyNotation)

fun DependencyHandlerScope.kspIosSimulatorArm64(
    dependencyNotation: Any,
) = add("kspIosSimulatorArm64", dependencyNotation)

fun DependencyHandlerScope.kspIosArm64(
    dependencyNotation: Any,
) = add("kspIosArm64", dependencyNotation)

fun DependencyHandlerScope.kspIosX64(
    dependencyNotation: Any,
) = add("kspIosX64", dependencyNotation)
