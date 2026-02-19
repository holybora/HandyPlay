package com.sls.handbook.feature.fever

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO
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
fun AnimatedValueText(
    value: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign? = null,
) {
    AnimatedContent(
        targetState = value,
        transitionSpec = {
            fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = FadeDurationMs))
        },
        label = "animatedValueText",
    ) { targetValue ->
        Text(
            text = targetValue,
            style = style,
            color = color,
            modifier = modifier,
            fontWeight = fontWeight,
            textAlign = textAlign,
        )
    }
}

@Composable
fun WeatherDescription(descriptionText: String) {
    AnimatedValueText(
        value = descriptionText,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
internal fun WeatherIconCard(
    iconUrl: String,
    iconContentDescription: String,
    temperatureText: String,
    weatherDescriptionText: String,
    feelsLikeText: String,
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
            AnimatedVisibility(
                visible = weatherDescriptionText.isNotBlank(),
                enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
                exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
            ) {
                WeatherDescription(descriptionText = weatherDescriptionText)
            }
            Spacer(modifier = Modifier.height(12.dp))

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
            Text(
                text = stringResource(R.string.fever_label_feels_like),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            AnimatedValueText(
                value = feelsLikeText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
internal fun HourlyForecastSection(hourlyForecasts: List<HourlyDisplayData>) {
    val feverColors = LocalFeverColors.current
    Column {
        Text(
            text = stringResource(R.string.fever_hourly_header),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
            ) {
                items(hourlyForecasts) { item ->
                    HourlyForecastItem(item = item, popColor = feverColors.iconBlue)
                }
            }
        }
    }
}

@Composable
internal fun HourlyForecastItem(item: HourlyDisplayData, popColor: Color) {
    Column(
        modifier = Modifier
            .width(72.dp)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = item.timeText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(4.dp))
        AsyncImage(
            model = item.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.temperatureText,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = item.popText,
            style = MaterialTheme.typography.labelSmall,
            color = popColor,
            textAlign = TextAlign.Center,
        )
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

@Preview(showBackground = true, device = PIXEL_9_PRO)
@Composable
private fun WeatherIconCardPreview() {
    FeverTheme {
        WeatherIconCard(
            iconUrl = "https://openweathermap.org/img/wn/03d@4x.png",
            iconContentDescription = "scattered clouds",
            temperatureText = "32°C",
            weatherDescriptionText = "Scattered clouds",
            feelsLikeText = "35°C",
            modifier = Modifier.height(320.dp),
        )
    }
}

@Preview
@Composable
private fun HourlyForecastSectionPreview() {
    FeverTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            HourlyForecastSection(
                hourlyForecasts = listOf(
                    HourlyDisplayData(
                        timeText = "Now",
                        iconUrl = "https://openweathermap.org/img/wn/03d@2x.png",
                        temperatureText = "32°C",
                        popText = "10%",
                    ),
                    HourlyDisplayData(
                        timeText = "1 PM",
                        iconUrl = "https://openweathermap.org/img/wn/10d@2x.png",
                        temperatureText = "33°C",
                        popText = "35%",
                    ),
                    HourlyDisplayData(
                        timeText = "2 PM",
                        iconUrl = "https://openweathermap.org/img/wn/04d@2x.png",
                        temperatureText = "31°C",
                        popText = "20%",
                    ),
                ),
            )
        }
    }
}
