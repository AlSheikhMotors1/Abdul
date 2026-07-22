package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SunriseNavyDark,
    secondary = SunriseGoldLight,
    tertiary = StatusGreen,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = SunriseNavy,
    onSecondary = Color.Black,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark
)

private val LightColorScheme = lightColorScheme(
    primary = SunriseNavy,
    onPrimary = Color.White,
    primaryContainer = SunriseGoldContainer,
    onPrimaryContainer = SunriseNavy,
    secondary = SunriseGold,
    onSecondary = Color.White,
    secondaryContainer = SurfaceVariantLight,
    onSecondaryContainer = SunriseNavy,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun SunriseSchoolTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
