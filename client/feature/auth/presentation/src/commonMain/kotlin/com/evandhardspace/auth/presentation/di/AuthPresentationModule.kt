package com.evandhardspace.auth.presentation.di

import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.auth.domain.validation.UsernameValidator
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
internal class AuthDomainModule {
    @Factory
    fun provideEmailValidator(): EmailValidator = EmailValidator()

    @Factory
    fun provideUsernameValidator(): UsernameValidator = UsernameValidator()

    @Factory
    fun providePasswordValidator(): PasswordValidator = PasswordValidator()
}

@ComponentScan("com.evandhardspace.auth.presentation")
@Module(includes = [AuthDomainModule::class])
class AuthPresentationModule
