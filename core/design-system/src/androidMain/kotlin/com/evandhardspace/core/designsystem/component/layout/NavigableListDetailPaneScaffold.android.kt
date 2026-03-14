package com.evandhardspace.core.designsystem.component.layout

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

@Composable
internal actual fun <T> ThreePaneScaffoldPredictiveBackHandler(
    navigator: ThreePaneScaffoldNavigator<T>,
    backBehavior: BackNavigationBehavior
) {
    key(navigator, backBehavior) {
        PredictiveBackHandler(enabled = navigator.canNavigateBack(backBehavior)) { progress ->
            // code for gesture back started
            try {
                progress.collect { backEvent ->
                    navigator.seekBack(
                        backBehavior,
                        fraction =
                            backProgressToStateProgress(
                                progress = backEvent.progress,
                                scaffoldValue = navigator.scaffoldValue,
                            ),
                    )
                }
                // code for completion
                navigator.navigateBack(backBehavior)
            } catch (_: CancellationException) {
                // code for cancellation
                withContext(NonCancellable) {
                    navigator.seekBack(backBehavior, fraction = 0f)
                }
            }
        }
    }
}

private fun backProgressToStateProgress(
    progress: Float,
    scaffoldValue: ThreePaneScaffoldValue
): Float =
    ThreePaneScaffoldPredictiveBackEasing.transform(progress) *
            when (scaffoldValue.expandedCount) {
                1 -> SinglePaneProgressRatio
                2 -> DualPaneProgressRatio
                else -> TriplePaneProgressRatio
            }

private val ThreePaneScaffoldPredictiveBackEasing: Easing = CubicBezierEasing(0.1f, 0.1f, 0f, 1f)
private const val SinglePaneProgressRatio: Float = 0.1f
private const val DualPaneProgressRatio: Float = 0.15f
private const val TriplePaneProgressRatio: Float = 0.2f

private val ThreePaneScaffoldValue.expandedCount: Int
    get() {
        var count = 0
        if (primary == PaneAdaptedValue.Expanded) {
            count++
        }
        if (secondary == PaneAdaptedValue.Expanded) {
            count++
        }
        if (tertiary == PaneAdaptedValue.Expanded) {
            count++
        }
        return count
    }
