package com.evandhardspace.chat.domain.usercase

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither

interface LogoutUseCase {
    suspend operator fun invoke(): EmptyEither<DataError>
}