package com.evandhardspace.core.domain.validation.rule.password

interface PasswordValidator {
    fun validate(password: String): Boolean
}

fun PasswordValidator(): PasswordValidator =
    DefaultPasswordValidator()

internal class DefaultPasswordValidator : PasswordValidator {
    private val rule = defaultPasswordValidationRule()

    override fun validate(password: String): Boolean =
        rule.validate(password)
}
