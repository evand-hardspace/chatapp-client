package com.evandhardspace.core.designsystem.component.brand

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chatapp.core.design_system.generated.resources.Res
import chatapp.core.design_system.generated.resources.a11y_chatapp_logo
import chatapp.core.design_system.generated.resources.logo_chatapp_dark
import chatapp.core.design_system.generated.resources.logo_chatapp_light
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChatAppBrandLogo(
    modifier: Modifier = Modifier,
) {
    Image(
        imageVector = vectorResource(
            if (isSystemInDarkTheme()) Res.drawable.logo_chatapp_dark
            else Res.drawable.logo_chatapp_light,
        ),
        contentDescription = stringResource(Res.string.a11y_chatapp_logo),
        modifier = modifier,
    )
}
