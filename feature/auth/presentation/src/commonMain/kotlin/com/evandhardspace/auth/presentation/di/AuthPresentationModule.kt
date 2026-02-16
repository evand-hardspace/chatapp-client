package com.evandhardspace.auth.presentation.di

import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.auth.domain.validation.UsernameValidator
import com.evandhardspace.auth.presentation.email_verifiaction.EmailVerificationViewModel
import com.evandhardspace.auth.presentation.login.LoginViewModel
import com.evandhardspace.auth.presentation.register.RegisterViewModel
import com.evandhardspace.auth.presentation.register_success.RegisterSuccessViewModel
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val authDomainModule = module {
    factory<EmailValidator> { EmailValidator() }
    factory<UsernameValidator> { UsernameValidator() }
    factory<PasswordValidator> { PasswordValidator() }
}

val authPresentationModule = module {
    includes(authDomainModule)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::RegisterSuccessViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModelOf(::LoginViewModel)
}