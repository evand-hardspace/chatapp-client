package com.evandhardspace.chat.data.chat.usecase

import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.usercase.LogoutUseCase
import com.evandhardspace.core.common.di.ApplicationScope
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.notification.DeviceTokenRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.asFailure
import com.evandhardspace.core.domain.util.asSuccess
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first

@ContributesBinding(AppScope::class)
@Inject
class DefaultLogoutUseCase(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
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
