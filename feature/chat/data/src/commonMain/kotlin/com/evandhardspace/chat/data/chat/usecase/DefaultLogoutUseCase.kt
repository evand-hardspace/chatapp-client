package com.evandhardspace.chat.data.chat.usecase

import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.usercase.LogoutUseCase
import com.evandhardspace.core.common.di.ApplicationScope
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.notification.DeviceTokenRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.asFailure
import com.evandhardspace.core.domain.util.asSuccess
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
class DefaultLogoutUseCase(
    private val authRepository: AuthRepository,
    private val sessionRepository: MutableSessionRepository,
    private val deviceRepository: DeviceTokenRepository,
    private val chatRepository: ChatRepository,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
) : LogoutUseCase {
    override suspend fun invoke(): EmptyEither<DataError> {
        val authState =
            sessionRepository.authState.filterIsInstance<AuthState.Authenticated>().first()

        return applicationScope.async {
            deviceRepository.unregisterToken(authState.refreshToken)
                .onSuccess {
                    authRepository.logout(authState.refreshToken)
                        .onSuccess {
                            chatRepository.clear()
                            sessionRepository.logout()
                            return@async Unit.asSuccess()
                        }
                        .onFailure { error ->
                            return@async error.asFailure()
                        }
                }
                .onFailure { error ->
                    return@async error.asFailure()
                }
        }.await()
    }
}
