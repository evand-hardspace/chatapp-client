package com.evandhardspace.auth.domain.validation

interface EmailValidator {
    fun validate(email: CharSequence): Boolean
}

fun EmailValidator(): EmailValidator =
    DefaultEmailValidator()

internal class DefaultEmailValidator : EmailValidator {
    override fun validate(email: CharSequence): Boolean =
        email matches EMAIL_PATTERN.toRegex()
}

private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
