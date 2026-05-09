package com.evandhardspace.auth.domain.validation

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

interface EmailValidator {
    fun validate(email: CharSequence): Boolean
}

@ContributesBinding(AppScope::class)
@Inject
internal class DefaultEmailValidator : EmailValidator {
    override fun validate(email: CharSequence): Boolean =
        email matches EMAIL_PATTERN.toRegex()
}

private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
