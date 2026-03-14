package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.PaneExpansionState
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldPaneScope
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> NavigableListDetailPaneScaffold(
    navigator: ThreePaneScaffoldNavigator<T>,
    listPane: @Composable ThreePaneScaffoldPaneScope.() -> Unit,
    detailPane: @Composable ThreePaneScaffoldPaneScope.() -> Unit,
    modifier: Modifier = Modifier,
    extraPane: (@Composable ThreePaneScaffoldPaneScope.() -> Unit)? = null,
    defaultBackBehavior: BackNavigationBehavior =
        BackNavigationBehavior.PopUntilScaffoldValueChange,
    paneExpansionDragHandle: (@Composable ThreePaneScaffoldScope.(PaneExpansionState) -> Unit)? =
        null,
    paneExpansionState: PaneExpansionState? = null,
) {
    ThreePaneScaffoldPredictiveBackHandler(
        navigator = navigator,
        backBehavior = defaultBackBehavior,
    )

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        scaffoldState = navigator.scaffoldState,
        detailPane = detailPane,
        listPane = listPane,
        extraPane = extraPane,
        paneExpansionDragHandle = paneExpansionDragHandle,
        paneExpansionState = paneExpansionState,
    )
}

@Composable
internal expect fun <T> ThreePaneScaffoldPredictiveBackHandler(
    navigator: ThreePaneScaffoldNavigator<T>,
    backBehavior: BackNavigationBehavior,
)
