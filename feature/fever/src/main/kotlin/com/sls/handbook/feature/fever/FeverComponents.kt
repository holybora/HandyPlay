package com.sls.handbook.feature.fever

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sls.handbook.feature.fever.theme.FeverTheme
import com.sls.handbook.feature.fever.theme.LocalFeverColors

@Composable
internal fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val feverColors = LocalFeverColors.current
    Surface(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.04f),
            )
            .border(
                width = 1.dp,
                color = feverColors.glassBorder,
                shape = RoundedCornerShape(16.dp),
            ),
        shape = RoundedCornerShape(16.dp),
        color = feverColors.glassSurface,
        content = content,
    )
}

@Composable
internal fun GlassDetailCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = FadeDurationMs))
                },
                label = "detailCardValue",
            ) { targetValue ->
                Text(
                    text = targetValue,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
internal fun WeatherIconCard(
    iconUrl: String,
    iconContentDescription: String,
    temperatureText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Crossfade(
                targetState = iconUrl,
                animationSpec = tween(durationMillis = FadeDurationMs),
                label = "weatherIconCrossfade",
            ) { targetIconUrl ->
                if (targetIconUrl.isNotBlank()) {
                    AsyncImage(
                        model = targetIconUrl,
                        contentDescription = iconContentDescription,
                        modifier = Modifier.size(100.dp),
                    )
                }
            }
            AnimatedContent(
                targetState = temperatureText,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)) togetherWith
                        fadeOut(animationSpec = tween(durationMillis = FadeDurationMs))
                },
                label = "temperatureText",
            ) { targetTemp ->
                Text(
                    text = targetTemp,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

// --- Previews ---

@Preview
@Composable
private fun GlassCardPreview() {
    FeverTheme {
        GlassCard(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Glass Card Content",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun GlassDetailCardPreview() {
    FeverTheme {
        GlassDetailCard(
            label = "Pressure",
            value = "1008 hPa",
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview
@Composable
private fun WeatherIconCardPreview() {
    FeverTheme {
        WeatherIconCard(
            iconUrl = "https://openweathermap.org/img/wn/03d@4x.png",
            iconContentDescription = "scattered clouds",
            temperatureText = "32°C",
            modifier = Modifier
                .padding(16.dp)
                .size(160.dp),
        )
    }
}
