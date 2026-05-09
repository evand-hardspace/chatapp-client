package com.evandhardspace.core.navigation.deeplink

import androidx.navigation.NavController
import com.evandhardspace.core.common.coroutines.DispatcherProvider
import com.evandhardspace.core.common.di.ApplicationScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SingleIn(AppScope::class)
@Inject
class DeeplinkProcessor(
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    private val interceptors: Set<DeeplinkInterceptor>,
    private val dispatchers: DispatcherProvider,
) {

    val isProcessing: StateFlow<Boolean>
        field = MutableStateFlow(false)

    fun process(
        uri: String,
        navController: NavController,
        onDeeplinkFallback: () -> Unit,
    ) {
        applicationScope.launch(dispatchers.main) {
            try {
                isProcessing.update { true }
                interceptors.forEach { interceptor ->
                    if (interceptor.process(uri, navController)) {
                        return@launch
                    }
                }
                onDeeplinkFallback()
            } finally {
                isProcessing.update { false }
            }
        }
    }
}
