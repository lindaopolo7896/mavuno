package com.example.mavuno.ui.theme



import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = CropPalette.TealDeep,
    onPrimary = CropPalette.White,
    primaryContainer = CropPalette.MintPale,
    onPrimaryContainer = CropPalette.TealDeep,

    secondary = CropPalette.TealDeep,
    onSecondary = CropPalette.White,

    tertiary = CropPalette.AmberDeep,
    onTertiary = CropPalette.TealDeep,

    background = CropPalette.MintWhite,
    onBackground = CropPalette.InkGreen,

    surface = CropPalette.MintWhite,
    onSurface = CropPalette.InkGreen,

    error = CropPalette.ErrorRed,
    onError = CropPalette.White
)

private val DarkColors = darkColorScheme(
    primary = CropPalette.MintAccent,
    onPrimary = CropPalette.ForestBase,
    primaryContainer = CropPalette.ForestContainer,
    onPrimaryContainer = CropPalette.MintBright,

    secondary = CropPalette.MintBright,
    onSecondary = CropPalette.ForestBase,

    tertiary = CropPalette.AmberBright,
    onTertiary = CropPalette.AmberDark,



    background = CropPalette.ForestBase,
    onBackground = CropPalette.FogWhite,

    surface = CropPalette.ForestBase,
    onSurface = CropPalette.FogWhite,

    error = CropPalette.ErrorBright,
    onError = CropPalette.ErrorDark
)

@Immutable
data class CropSemanticColors(
    val heading: Color,
    val tag:Color,
    val success: Color,
    val onSuccess: Color,
    val inputBorder: Color,
    val inputBackground:Color,
    val disabledContent: Color,
    val disabledContainer: Color,
    val cameraOverlay: Color,
    val onCameraOverlay: Color,
    val notificationDot: Color
)

private val LightSemanticColors = CropSemanticColors(
    heading = CropPalette.TealDeep,
    tag= CropPalette.GreyGreen,
    success = CropPalette.GreenIndicator,
    onSuccess = CropPalette.White,
    inputBorder = CropPalette.Black.copy(alpha = 0.45f),
    inputBackground = CropPalette.Transparent,
    disabledContent = CropPalette.InkGreen.copy(alpha = 0.38f),
    disabledContainer = CropPalette.InkGreen.copy(alpha = 0.12f),
    cameraOverlay = CropPalette.TealDeep.copy(alpha = 0.80f),
    onCameraOverlay = CropPalette.White,
    notificationDot = CropPalette.CoralNotification
)

private val DarkSemanticColors = CropSemanticColors(
    heading = CropPalette.FogWhite,
    tag = CropPalette.GreyGreenDark,
    success = CropPalette.MintAccent,
    onSuccess = CropPalette.ForestBase,
    inputBorder = CropPalette.SageText,
    inputBackground = CropPalette.ForestRaised,
    disabledContent = CropPalette.FogWhite.copy(alpha = 0.38f),
    disabledContainer = CropPalette.FogWhite.copy(alpha = 0.12f),
    cameraOverlay = CropPalette.ForestBase.copy(alpha = 0.80f),
    onCameraOverlay = CropPalette.White,
    notificationDot = CropPalette.CoralNotification
)

private val LocalCropSemanticColors =
    staticCompositionLocalOf<CropSemanticColors> {
        LightSemanticColors
    }

object CropTheme {
    val colors: CropSemanticColors
        @Composable
        @ReadOnlyComposable
        get() = LocalCropSemanticColors.current
}

@Composable
fun MavunoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    val semanticColors =
        if (darkTheme) DarkSemanticColors else LightSemanticColors

    CompositionLocalProvider(
        LocalCropSemanticColors provides semanticColors
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography(),
            shapes = Shapes(),
            content = content
        )
    }
}
