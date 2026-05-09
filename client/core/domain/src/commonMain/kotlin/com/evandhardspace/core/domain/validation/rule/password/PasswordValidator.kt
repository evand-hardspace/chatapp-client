package com.evandhardspace.core.domain.validation.rule.password

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

interface PasswordValidator {
    fun validate(password: String): Boolean
}

@ContributesBinding(AppScope::class)
@Inject
internal class DefaultPasswordValidator : PasswordValidator {
    private val rule = defaultPasswordValidationRule()

    override fun validate(password: String): Boolean =
        rule.validate(password)
}
