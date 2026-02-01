package com.evandhardspace.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

val MaterialTheme.paddings: ChatAppPaddings
    @Composable
    @ReadOnlyComposable
    get() = LocalChatAppPaddings.current

@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

val LightExtendedColors = ExtendedColors(
    primaryHover = ChatAppBrand600,
    destructiveHover = ChatAppRed600,
    destructiveSecondaryOutline = ChatAppRed200,
    disabledOutline = ChatAppBase200,
    disabledFill = ChatAppBase150,
    successOutline = ChatAppBrand100,
    success = ChatAppBrand600,
    onSuccess = ChatAppBase0,
    secondaryFill = ChatAppBase100,

    textPrimary = ChatAppBase1000,
    textTertiary = ChatAppBase800,
    textSecondary = ChatAppBase900,
    textPlaceholder = ChatAppBase700,
    textDisabled = ChatAppBase400,

    surfaceLower = ChatAppBase100,
    surfaceHigher = ChatAppBase100,
    surfaceOutline = ChatAppBase1000Alpha14,
    overlay = ChatAppBase1000Alpha80,

    accentBlue = ChatAppBlue,
    accentPurple = ChatAppPurple,
    accentViolet = ChatAppViolet,
    accentPink = ChatAppPink,
    accentOrange = ChatAppOrange,
    accentYellow = ChatAppYellow,
    accentGreen = ChatAppGreen,
    accentTeal = ChatAppTeal,
    accentLightBlue = ChatAppLightBlue,
    accentGrey = ChatAppGrey,

    cakeViolet = ChatAppCakeLightViolet,
    cakeGreen = ChatAppCakeLightGreen,
    cakeBlue = ChatAppCakeLightBlue,
    cakePink = ChatAppCakeLightPink,
    cakeOrange = ChatAppCakeLightOrange,
    cakeYellow = ChatAppCakeLightYellow,
    cakeTeal = ChatAppCakeLightTeal,
    cakePurple = ChatAppCakeLightPurple,
    cakeRed = ChatAppCakeLightRed,
    cakeMint = ChatAppCakeLightMint,
)

val DarkExtendedColors = ExtendedColors(
    primaryHover = ChatAppBrand600,
    destructiveHover = ChatAppRed600,
    destructiveSecondaryOutline = ChatAppRed200,
    disabledOutline = ChatAppBase900,
    disabledFill = ChatAppBase1000,
    successOutline = ChatAppBrand500Alpha40,
    success = ChatAppBrand500,
    onSuccess = ChatAppBase1000,
    secondaryFill = ChatAppBase900,

    textPrimary = ChatAppBase0,
    textTertiary = ChatAppBase200,
    textSecondary = ChatAppBase150,
    textPlaceholder = ChatAppBase400,
    textDisabled = ChatAppBase500,

    surfaceLower = ChatAppBase1000,
    surfaceHigher = ChatAppBase900,
    surfaceOutline = ChatAppBase100Alpha10Alt,
    overlay = ChatAppBase1000Alpha80,

    accentBlue = ChatAppBlue,
    accentPurple = ChatAppPurple,
    accentViolet = ChatAppViolet,
    accentPink = ChatAppPink,
    accentOrange = ChatAppOrange,
    accentYellow = ChatAppYellow,
    accentGreen = ChatAppGreen,
    accentTeal = ChatAppTeal,
    accentLightBlue = ChatAppLightBlue,
    accentGrey = ChatAppGrey,

    cakeViolet = ChatAppCakeDarkViolet,
    cakeGreen = ChatAppCakeDarkGreen,
    cakeBlue = ChatAppCakeDarkBlue,
    cakePink = ChatAppCakeDarkPink,
    cakeOrange = ChatAppCakeDarkOrange,
    cakeYellow = ChatAppCakeDarkYellow,
    cakeTeal = ChatAppCakeDarkTeal,
    cakePurple = ChatAppCakeDarkPurple,
    cakeRed = ChatAppCakeDarkRed,
    cakeMint = ChatAppCakeDarkMint,
)

val LightColorScheme = lightColorScheme(
    primary = ChatAppBrand500,
    onPrimary = ChatAppBrand1000,
    primaryContainer = ChatAppBrand100,
    onPrimaryContainer = ChatAppBrand900,

    secondary = ChatAppBase700,
    onSecondary = ChatAppBase0,
    secondaryContainer = ChatAppBase100,
    onSecondaryContainer = ChatAppBase900,

    tertiary = ChatAppBrand900,
    onTertiary = ChatAppBase0,
    tertiaryContainer = ChatAppBrand100,
    onTertiaryContainer = ChatAppBrand1000,

    error = ChatAppRed500,
    onError = ChatAppBase0,
    errorContainer = ChatAppRed200,
    onErrorContainer = ChatAppRed600,

    background = ChatAppBrand1000,
    onBackground = ChatAppBase0,
    surface = ChatAppBase0,
    onSurface = ChatAppBase1000,
    surfaceVariant = ChatAppBase100,
    onSurfaceVariant = ChatAppBase900,

    outline = ChatAppBase1000Alpha8,
    outlineVariant = ChatAppBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = ChatAppBrand500,
    onPrimary = ChatAppBrand1000,
    primaryContainer = ChatAppBrand900,
    onPrimaryContainer = ChatAppBrand500,

    secondary = ChatAppBase400,
    onSecondary = ChatAppBase1000,
    secondaryContainer = ChatAppBase900,
    onSecondaryContainer = ChatAppBase150,

    tertiary = ChatAppBrand500,
    onTertiary = ChatAppBase1000,
    tertiaryContainer = ChatAppBrand900,
    onTertiaryContainer = ChatAppBrand500,

    error = ChatAppRed500,
    onError = ChatAppBase0,
    errorContainer = ChatAppRed600,
    onErrorContainer = ChatAppRed200,

    background = ChatAppBase1000,
    onBackground = ChatAppBase0,
    surface = ChatAppBase950,
    onSurface = ChatAppBase0,
    surfaceVariant = ChatAppBase900,
    onSurfaceVariant = ChatAppBase150,

    outline = ChatAppBase100Alpha10,
    outlineVariant = ChatAppBase800,
)
