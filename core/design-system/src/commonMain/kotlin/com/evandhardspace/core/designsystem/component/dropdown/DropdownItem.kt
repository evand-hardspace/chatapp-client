package com.evandhardspace.core.designsystem.component.dropdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class DropdownItem(
    val title: String,
    val contentColor: Color,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
)
