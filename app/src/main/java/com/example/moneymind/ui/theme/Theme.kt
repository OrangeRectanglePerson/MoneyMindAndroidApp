package com.example.moneymind.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = LightCyan.copy(red = 0.2f, blue = 0.4f, green = 0.4f),

    surface = Teal.copy(alpha = 0.75f),

    onPrimary = Color.White
)

private val LightColorPalette = lightColors(
    primary = LibreOfficeBlue,
    primaryVariant = DarkLibreOfficeBlue,
    secondary = MediumTurquoise,
    secondaryVariant = DarkCyan,
    background = LightCyan,

    surface = PaleTurquoise.copy(alpha = 0.75f),
    onPrimary = Color.Black

    /* Other default colors to override

    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CSProjectTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}