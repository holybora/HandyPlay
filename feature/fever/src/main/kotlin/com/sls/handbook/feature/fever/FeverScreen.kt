package com.sls.handbook.feature.fever

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.feature.fever.theme.FeverTheme
import com.sls.handbook.feature.fever.theme.IconTeal
import com.sls.handbook.feature.fever.theme.LocalFeverColors
import com.theapache64.rebugger.Rebugger

@Composable
fun FeverScreen(
    uiState: FeverUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Rebugger(composableName = "FeverScreen", trackMap = mapOf("uiState" to uiState))

    val feverColors = LocalFeverColors.current
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(feverColors.gradientTop, feverColors.gradientBottom),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush),
    ) {
        WeatherContent(weatherDisplay = uiState.weatherDisplay)

        FloatingActionButton(
            onClick = onRefresh,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp),
        ) {
            if (uiState is FeverUiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.fever_refresh_location),
                )
            }
        }
    }
}

@Composable
private fun WeatherContent(weatherDisplay: WeatherDisplayData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp),
    ) {
        AnimatedVisibility(
            visible = weatherDisplay.iconUrl.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = 450)),
            exit = fadeOut(animationSpec = tween(durationMillis = 450)),
        ) {
            HeroSection(weatherDisplay = weatherDisplay)
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = weatherDisplay.locationName.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = 450)),
            exit = fadeOut(animationSpec = tween(durationMillis = 450)),
        ) {
            LocationHeader(locationName = weatherDisplay.locationName)
        }
        Spacer(modifier = Modifier.height(12.dp))
        AnimatedVisibility(
            visible = weatherDisplay.descriptionText.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = 450)),
            exit = fadeOut(animationSpec = tween(durationMillis = 450)),
        ) {
            WeatherDescription(descriptionText = weatherDisplay.descriptionText)
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = weatherDisplay.feelsLikeText.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = 450)),
            exit = fadeOut(animationSpec = tween(durationMillis = 450)),
        ) {
            Column {
                Text(
                    text = stringResource(R.string.fever_details_header),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(12.dp))
                DetailsSection(weatherDisplay = weatherDisplay)
            }
        }
        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
private fun HeroSection(weatherDisplay: WeatherDisplayData) {
    val feverColors = LocalFeverColors.current
    Row(
        modifier = Modifier
            .height(320.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        WeatherIconCard(
            iconUrl = weatherDisplay.iconUrl,
            iconContentDescription = weatherDisplay.iconContentDescription,
            temperatureText = weatherDisplay.temperatureText,
            modifier = Modifier
                .width(160.dp)
                .fillMaxHeight(),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatPill(
                icon = Icons.Default.Thermostat,
                iconBackgroundColor = feverColors.iconOrange,
                value = weatherDisplay.highLowText,
                label = stringResource(R.string.fever_label_high_low),
                modifier = Modifier.weight(1f),
            )
            StatPill(
                icon = Icons.Default.Air,
                iconBackgroundColor = feverColors.iconBlue,
                value = weatherDisplay.windText,
                label = stringResource(R.string.fever_label_wind),
                modifier = Modifier.weight(1f),
            )
            StatPill(
                icon = Icons.Default.WaterDrop,
                iconBackgroundColor = feverColors.iconTeal,
                value = weatherDisplay.humidityText,
                label = stringResource(R.string.fever_label_humidity),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun StatPill(
    icon: ImageVector,
    iconBackgroundColor: Color,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    GlassCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = iconBackgroundColor.copy(alpha = 0.15f),
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconBackgroundColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedValueText(
                value = value,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LocationHeader(locationName: String) {
    AnimatedValueText(
        value = locationName,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun WeatherDescription(descriptionText: String) {
    AnimatedValueText(
        value = descriptionText,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun DetailsSection(weatherDisplay: WeatherDisplayData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        GlassDetailCard(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(R.string.fever_label_feels_like),
            value = weatherDisplay.feelsLikeText,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.fever_label_pressure),
                value = weatherDisplay.pressureText,
            )
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.fever_label_visibility),
                value = weatherDisplay.visibilityText,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.fever_label_latitude),
                value = weatherDisplay.latitudeText,
            )
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.fever_label_longitude),
                value = weatherDisplay.longitudeText,
            )
        }
    }
}

@Composable
private fun AnimatedValueText(
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
            fadeIn(animationSpec = tween(durationMillis = 450)) togetherWith
                fadeOut(animationSpec = tween(durationMillis = 450))
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

// --- Preview Data ---

private val previewWeatherDisplay = WeatherDisplayData(
    temperatureText = "32°C",
    iconUrl = "https://openweathermap.org/img/wn/03d@4x.png",
    iconContentDescription = "scattered clouds",
    highLowText = "H:35° L:28°",
    windText = "4.2 m/s",
    humidityText = "78%",
    locationName = "Surabaya, ID",
    descriptionText = "Scattered clouds",
    feelsLikeText = "38°C",
    pressureText = "1008 hPa",
    visibilityText = "8 km",
    latitudeText = "-7.2575",
    longitudeText = "112.7521",
)

// --- Previews ---

@Preview
@Composable
private fun FeverScreenLoadingPreview() {
    FeverTheme {
        FeverScreen(uiState = FeverUiState.Loading, onRefresh = {})
    }
}

@Preview
@Composable
private fun FeverScreenSuccessPreview() {
    FeverTheme {
        FeverScreen(uiState = FeverUiState.Success(previewWeatherDisplay), onRefresh = {})
    }
}

@Preview
@Composable
private fun FeverScreenErrorPreview() {
    FeverTheme {
        FeverScreen(
            uiState = FeverUiState.Error("Unable to determine location"),
            onRefresh = {},
        )
    }
}

@Preview
@Composable
private fun StatPillPreview() {
    FeverTheme {
        StatPill(
            icon = Icons.Default.WaterDrop,
            iconBackgroundColor = IconTeal,
            value = "78%",
            label = "Humidity",
            modifier = Modifier
                .padding(16.dp)
                .width(120.dp)
                .height(80.dp),
        )
    }
}

@Preview
@Composable
private fun LocationHeaderPreview() {
    FeverTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LocationHeader(locationName = previewWeatherDisplay.locationName)
        }
    }
}

@Preview
@Composable
private fun WeatherDescriptionPreview() {
    FeverTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            WeatherDescription(descriptionText = previewWeatherDisplay.descriptionText)
        }
    }
}

@Preview
@Composable
private fun DetailsSectionPreview() {
    FeverTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DetailsSection(weatherDisplay = previewWeatherDisplay)
        }
    }
}
