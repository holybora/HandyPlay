package com.sls.handbook.feature.fever.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val FeverColorScheme = lightColorScheme(
    primary = SoftBlue,
    onPrimary = Color.White,
    primaryContainer = SkyBlueLight,
    onPrimaryContainer = NavyDark,
    secondary = IconCyan,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD4EEFB),
    onSecondaryContainer = NavyDark,
    tertiary = IconOrange,
    onTertiary = Color.White,
    background = SkyBlueWhite,
    onBackground = NavyDark,
    surface = GlassWhite,
    onSurface = NavyDark,
    surfaceVariant = Color(0xFFEDF4FB),
    onSurfaceVariant = GrayMedium,
    outline = GlassWhiteBorder,
    outlineVariant = Color(0x1A000000),
    error = CoralRed,
    onError = Color.White,
)

@Composable
fun FeverTheme(
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalFeverColors provides FeverExtendedColors(),
    ) {
        MaterialTheme(
            colorScheme = FeverColorScheme,
            typography = FeverTypography,
            content = content,
        )
    }
}
