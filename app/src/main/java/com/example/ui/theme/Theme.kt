package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CartoonColorScheme = lightColorScheme(
    primary = BunnyOrange,
    onPrimary = Color.White,
    primaryContainer = BunnyYellow,
    onPrimaryContainer = BunnyDarkText,
    secondary = BunnySkyBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F7FA),
    onSecondaryContainer = BunnyDarkText,
    tertiary = BunnyCoral,
    onTertiary = Color.White,
    background = BunnyCream,
    onBackground = BunnyDarkText,
    surface = Color.White,
    onSurface = BunnyDarkText,
    surfaceVariant = BunnyCloud,
    onSurfaceVariant = BunnyDarkText
)

@Composable
fun ABCBunnyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CartoonColorScheme,
        typography = Typography,
        content = content
    )
}
