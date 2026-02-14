package com.sls.handbook.core.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = PrimaryForeground,
    primaryContainer = Zinc100,
    onPrimaryContainer = Zinc900,
    secondary = Zinc100,
    onSecondary = Zinc900,
    secondaryContainer = Zinc100,
    onSecondaryContainer = Zinc900,
    tertiary = Zinc600,
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Zinc950,
    surface = Color.White,
    onSurface = Zinc950,
    surfaceVariant = Zinc100,
    onSurfaceVariant = Zinc600,
    outline = Zinc200,
    outlineVariant = Zinc100,
    error = Destructive,
    onError = DestructiveForeground
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = PrimaryForegroundDark,
    primaryContainer = Zinc800,
    onPrimaryContainer = Zinc50,
    secondary = Zinc800,
    onSecondary = Zinc50,
    secondaryContainer = Zinc800,
    onSecondaryContainer = Zinc50,
    tertiary = Zinc400,
    onTertiary = Zinc950,
    background = Zinc950,
    onBackground = Zinc50,
    surface = Zinc950,
    onSurface = Zinc50,
    surfaceVariant = Zinc900,
    onSurfaceVariant = Zinc400,
    outline = Zinc800,
    outlineVariant = Zinc800,
    error = Destructive,
    onError = DestructiveForeground
)

@Composable
fun HandyPlayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
