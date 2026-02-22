package com.evandhardspace.core.navigation.deeplink

import androidx.navigation.NavController
import com.evandhardspace.core.common.coroutines.DispatcherProvider
import com.evandhardspace.core.common.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

@Singleton
class DeeplinkProcessor(
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    private val interceptors: List<DeeplinkInterceptor>,
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
