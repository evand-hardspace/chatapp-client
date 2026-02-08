package com.evandhardspace.auth.domain.validation

interface UsernameValidator {
    fun validate(username: CharSequence): Boolean
}

fun UsernameValidator(): UsernameValidator =
    DefaultUsernameValidator()

class DefaultUsernameValidator : UsernameValidator {
    override fun validate(username: CharSequence): Boolean =
        username.length in 3..20
}