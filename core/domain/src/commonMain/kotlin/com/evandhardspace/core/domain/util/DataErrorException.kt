package com.evandhardspace.core.domain.util

class DataErrorException(
    val error: DataError,
): Exception()
