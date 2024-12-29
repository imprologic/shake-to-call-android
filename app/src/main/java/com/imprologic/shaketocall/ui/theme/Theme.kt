package com.imprologic.shaketocall.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF1B1B1B),
    tertiary = Color(0xFFF1F8E9),
    onTertiary = Color(0xFF000000),

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1B1B1B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1B1B1B),
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF)
)


private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF4CAF50),
    onPrimary = Color(0xFF1B1B1B),
    secondary = Color(0xFF66BB6A),
    onSecondary = Color(0xFF1B1B1B),
    tertiary = Color(0xFF1B1B1B),
    onTertiary = Color(0xFFFFFFFF),

    background = Color(0xFF1B1B1B),
    onBackground = Color(0xFFE8F5E9),
    surface = Color(0xFF263238),
    onSurface = Color(0xFFE8F5E9),
    error = Color(0xFFEF5350),
    onError = Color(0xFF1B1B1B)
)

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorPalette
        else -> LightColorPalette
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}