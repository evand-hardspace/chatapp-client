package com.evandhardspace.auth.presentation.email_verifiaction

internal data class EmailVerificationState(
    val verification: VerificationState = VerificationState.Verifying,
) {
    enum class VerificationState {
        Verifying,
        Verified,
        Error,
    }
}