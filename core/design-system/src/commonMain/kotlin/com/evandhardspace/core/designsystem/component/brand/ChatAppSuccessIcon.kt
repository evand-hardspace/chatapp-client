package com.evandhardspace.core.designsystem.component.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chatapp.core.design_system.generated.resources.Res
import chatapp.core.design_system.generated.resources.success_checkmark_icon
import com.evandhardspace.core.designsystem.theme.extended
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChatAppSuccessIcon(
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark_icon),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier,
    )
}