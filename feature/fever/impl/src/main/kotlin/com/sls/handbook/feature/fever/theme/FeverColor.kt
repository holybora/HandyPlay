package com.sls.handbook.feature.fever.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Gradient background
val SkyBlueLight = Color(0xFFB0D4F1)
val SkyBlueWhite = Color(0xFFE8F2FB)

// Glass surface
val GlassWhite = Color(0xCCFFFFFF)
val GlassWhiteBorder = Color(0x33FFFFFF)

// Text
val NavyDark = Color(0xFF1A2138)
val GrayMedium = Color(0xFF6B7280)

// Stat icon accent colors
val IconOrange = Color(0xFFFF9500)
val IconBlue = Color(0xFF4A90D9)
val IconCyan = Color(0xFF5AC8FA)
val IconTeal = Color(0xFF34C3A0)

// Primary interactive
val SoftBlue = Color(0xFF5B9BD5)

// Error
val CoralRed = Color(0xFFFF6B6B)

@Immutable
data class FeverExtendedColors(
    val gradientTop: Color = SkyBlueLight,
    val gradientBottom: Color = SkyBlueWhite,
    val glassSurface: Color = GlassWhite,
    val glassBorder: Color = GlassWhiteBorder,
    val iconOrange: Color = IconOrange,
    val iconBlue: Color = IconBlue,
    val iconCyan: Color = IconCyan,
    val iconTeal: Color = IconTeal,
)

val LocalFeverColors = staticCompositionLocalOf { FeverExtendedColors() }
