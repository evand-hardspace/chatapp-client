package com.evandhardspace.core.navigation.deeplink

import androidx.navigation.NavController
import com.evandhardspace.core.common.coroutines.DispatcherProvider
import com.evandhardspace.core.common.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

@Singleton
class DeeplinkProcessor(
    @param:ApplicationScope private val applicationScope: CoroutineScope,
    private val interceptors: List<DeeplinkInterceptor>,
    private val dispatchers: DispatcherProvider,
) {
    fun process(
        uri: String,
        navController: NavController,
        onDeeplinkFallback: () -> Unit,
    ) {
        applicationScope.launch(dispatchers.main) {
            interceptors.forEach { interceptor ->
                if (interceptor.process(uri, navController)) {
                    return@launch
                }
            }
            onDeeplinkFallback()
        }
    }
}
