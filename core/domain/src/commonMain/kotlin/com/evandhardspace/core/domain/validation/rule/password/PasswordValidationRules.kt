package com.evandhardspace.core.domain.validation.rule.password

import com.evandhardspace.core.domain.validation.rule.ValidationRule
import com.evandhardspace.core.domain.validation.rule.and

internal class HasMinLengthRule : ValidationRule<CharSequence> {
    override fun validate(value: CharSequence): Boolean =
        value.length >= MIN_PASSWORD_LENGTH

    companion object {
        const val MIN_PASSWORD_LENGTH = 9
    }
}

internal class HasDigitRule : ValidationRule<CharSequence> {
    override fun validate(value: CharSequence): Boolean =
        value.any { it.isDigit() }
}

internal class HasUppercaseRule : ValidationRule<CharSequence> {
    override fun validate(value: CharSequence): Boolean =
        value.any { it.isUpperCase() }
}

internal fun defaultPasswordValidationRule(): ValidationRule<CharSequence> =
    HasMinLengthRule() and HasDigitRule() and HasUppercaseRule()
