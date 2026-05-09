package com.evandhardspace.auth.domain.validation

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

interface UsernameValidator {
    fun validate(username: CharSequence): Boolean
}

@Inject
@ContributesBinding(AppScope::class)
internal class DefaultUsernameValidator : UsernameValidator {
    override fun validate(username: CharSequence): Boolean =
        username.length in 3..20
}
