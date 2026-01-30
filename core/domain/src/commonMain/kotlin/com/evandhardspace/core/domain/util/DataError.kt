package com.evandhardspace.core.domain.util

sealed interface DataError: DomainError {
    enum class Remote: DataError {
        BadRequest,
        RequestTimeout,
        Unauthorized,
        Forbidden,
        NotFound,
        Conflict,
        TooManyRequests,
        NoInternet,
        PayloadTooLarge,
        ServerError,
        ServiceUnavailable,
        Serialization,
        Unknown,
    }

    enum class Local: DataError {
        DistFull,
        NotFound,
        Unknown,
    }
}