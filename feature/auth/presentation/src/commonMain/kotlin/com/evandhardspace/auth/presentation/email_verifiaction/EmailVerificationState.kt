package com.evandhardspace.auth.presentation.email_verifiaction

internal data class EmailVerificationState(
    val verification: VerificationState = VerificationState.Verifying,
) {
    sealed interface VerificationState {
        data object Verifying: VerificationState
        data class Verified(val isAuthenticated: Boolean): VerificationState
        data class Error(val isAuthenticated: Boolean): VerificationState
    }
}