package com.evandhardspace.core.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

context(builder: NavOptionsBuilder)
fun NavController.fullClearBackStack() {
    builder.popUpTo(graph.startDestinationId) {
        inclusive = true
    }
}
