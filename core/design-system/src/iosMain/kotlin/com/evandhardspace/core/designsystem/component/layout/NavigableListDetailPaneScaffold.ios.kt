package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventTransitionState
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
internal actual fun <T> ThreePaneScaffoldPredictiveBackHandler(
    navigator: ThreePaneScaffoldNavigator<T>,
    backBehavior: BackNavigationBehavior,
    onBackCompleted: (() -> Unit)?,
) {
    key(navigator, backBehavior) {
        val scope = rememberCoroutineScope()
        val navigationEventState = rememberNavigationEventState(NavigationEventInfo.None)
        val backReleaseAnimation = remember { BackReleaseAnimatable(scope, 0f) }
        var isBackInReleaseState by remember { mutableStateOf(false) }

        LaunchedEffect(navigationEventState.transitionState) {
            when (val state = navigationEventState.transitionState) {
                NavigationEventTransitionState.Idle -> Unit
                is NavigationEventTransitionState.InProgress -> {
                    val fraction = state.latestEvent.progress * ProgressRatio
                    backReleaseAnimation.snapTo(fraction)
                    navigator.seekBack(
                        backBehavior,
                        fraction = fraction
                    )
                }
            }
        }

        // Handle cancellation animation
        LaunchedEffect(isBackInReleaseState, backReleaseAnimation.value) {
            if (isBackInReleaseState) {
                navigator.seekBack(backBehavior, backReleaseAnimation.value)
            }
        }

        NavigationBackHandler(
            state = navigationEventState,
            isBackEnabled = navigator.canNavigateBack() && isBackInReleaseState.not(),

            onBackCancelled = {
                isBackInReleaseState = true
                backReleaseAnimation.animateTo(
                    targetValue = 0f,
                ) {
                    isBackInReleaseState = false
                    navigator.seekBack(backBehavior, 0f)
                }
            },

            onBackCompleted = {
                scope.launch {
                    onBackCompleted?.invoke()
                    navigator.navigateBack(backBehavior)
                    isBackInReleaseState = false
                }
            }
        )
    }
}

private class BackReleaseAnimatable(
    private val scope: CoroutineScope,
    initValue: Float,
) {

    private var job: Job? = null
    private val progress = Animatable(initValue)

    val value: Float
        get() = progress.value

    fun snapTo(targetValue: Float) {
        job?.cancel()
        job = scope.launch {
            progress.snapTo(targetValue)
        }
    }

    fun animateTo(
        targetValue: Float,
        onComplete: (suspend () -> Unit)? = null,
    ) {
        job?.cancel()
        job = scope.launch {
            progress.animateTo(
                targetValue = targetValue,
                animationSpec = tween(BackReleaseAnimationDurationMs),
            )
            onComplete?.invoke()
        }
    }
}

private const val BackReleaseAnimationDurationMs = 100
private const val ProgressRatio = 0.15f
