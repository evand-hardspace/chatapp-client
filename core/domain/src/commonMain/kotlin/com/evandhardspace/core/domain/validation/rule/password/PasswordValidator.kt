package com.evandhardspace.core.domain.validation.rule.password

import com.evandhardspace.core.domain.validation.rule.and

interface PasswordValidator {
    fun validate(password: CharSequence): Boolean
}

fun PasswordValidator(): PasswordValidator =
    DefaultPasswordValidator()

internal class DefaultPasswordValidator : PasswordValidator {
    private val rule = defaultPasswordValidationRule()

    override fun validate(password: CharSequence): Boolean =
        rule.validate(password)
}
