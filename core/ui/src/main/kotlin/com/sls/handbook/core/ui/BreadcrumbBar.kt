package com.sls.handbook.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ChipShape = RoundedCornerShape(21.dp)

@Composable
fun BreadcrumbBar(
    pathSegments: List<String>,
    modifier: Modifier = Modifier,
    onSegmentClick: (Int) -> Unit = {},
) {
    val isDark = isSystemInDarkTheme()
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        pathSegments.forEachIndexed { index, segment ->
            val isLast = index == pathSegments.lastIndex

            BreadcrumbChip(
                text = segment,
                containerColor = if (isLast) {
                    if (isDark) colors.surfaceVariant else colors.primary
                } else {
                    if (isDark) colors.surface else colors.surfaceVariant
                },
                contentColor = if (isLast) {
                    if (isDark) colors.onSurfaceVariant else colors.onPrimary
                } else {
                    if (isDark) colors.onSurfaceVariant else colors.onSurface
                },
                onClick = if (isLast) null else ({ onSegmentClick(index) }),
            )

            if (!isLast) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colors.onSurfaceVariant,
                    modifier = Modifier.size(21.dp),
                )
            }
        }
    }
}

@Composable
private fun BreadcrumbChip(
    text: String,
    containerColor: Color,
    contentColor: Color,
    onClick: (() -> Unit)?,
) {
    val chipModifier = if (onClick != null) {
        Modifier.clip(ChipShape).clickable(onClick = onClick)
    } else {
        Modifier
    }
    Surface(
        shape = ChipShape,
        color = containerColor,
        modifier = chipModifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
