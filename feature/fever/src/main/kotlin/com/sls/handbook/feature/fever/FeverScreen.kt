package com.sls.handbook.feature.fever

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sls.handbook.feature.fever.theme.FeverTheme
import com.sls.handbook.feature.fever.theme.IconTeal
import com.sls.handbook.feature.fever.theme.LocalFeverColors
import com.theapache64.rebugger.Rebugger

internal const val FadeDurationMs = 450

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
    val snackbarHostState = remember { SnackbarHostState() }
    val retryLabel = stringResource(R.string.fever_error_retry)
    val currentOnRefresh by rememberUpdatedState(onRefresh)

    if (uiState is FeverUiState.Error) {
        LaunchedEffect(uiState.message) {
            val result = snackbarHostState.showSnackbar(
                message = uiState.message,
                actionLabel = retryLabel,
            )
            if (result == SnackbarResult.ActionPerformed) {
                currentOnRefresh()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBrush),
    ) {
        WeatherContent(weatherDisplay = uiState.weatherDisplay)
        ErrorSnackbar(snackbarHostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        SwipeHintFab(
            isLoading = uiState is FeverUiState.Loading,
            icon = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(R.string.fever_swipe_right_hint),
            onClick = onRefresh,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp),
        )
    }
}

@Composable
private fun ErrorSnackbar(snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier
            .navigationBarsPadding()
            .padding(bottom = 96.dp),
    ) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            actionColor = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun SwipeHintFab(
    isLoading: Boolean,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(imageVector = icon, contentDescription = contentDescription)
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
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                WeatherIconCard(
                    iconUrl = weatherDisplay.iconUrl,
                    iconContentDescription = weatherDisplay.iconContentDescription,
                    temperatureText = weatherDisplay.temperatureText,
                    weatherDescriptionText = weatherDisplay.descriptionText,
                    feelsLikeText = weatherDisplay.feelsLikeText,
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight(),
                )
                StatPillsColumn(
                    weatherDisplay = weatherDisplay,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = weatherDisplay.locationName.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            LocationHeader(locationName = weatherDisplay.locationName)
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = weatherDisplay.hourlyForecasts.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            HourlyForecastSection(hourlyForecasts = weatherDisplay.hourlyForecasts)
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = weatherDisplay.feelsLikeText.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
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
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedVisibility(
            visible = weatherDisplay.forecast.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            ForecastSection(forecast = weatherDisplay.forecast)
        }
        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
private fun StatPillsColumn(weatherDisplay: WeatherDisplayData, modifier: Modifier = Modifier) {
    val feverColors = LocalFeverColors.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AnimatedVisibility(
            visible = weatherDisplay.highLowText.isNotBlank(),
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            StatPill(
                icon = Icons.Default.Thermostat,
                iconBackgroundColor = feverColors.iconOrange,
                value = weatherDisplay.highLowText,
                label = stringResource(R.string.fever_label_high_low),
                modifier = Modifier.fillMaxSize(),
            )
        }
        AnimatedVisibility(
            visible = weatherDisplay.windText.isNotBlank(),
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            StatPill(
                icon = Icons.Default.Air,
                iconBackgroundColor = feverColors.iconBlue,
                value = weatherDisplay.windText,
                label = stringResource(R.string.fever_label_wind),
                modifier = Modifier.fillMaxSize(),
            )
        }
        AnimatedVisibility(
            visible = weatherDisplay.humidityText.isNotBlank(),
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
            StatPill(
                icon = Icons.Default.WaterDrop,
                iconBackgroundColor = feverColors.iconTeal,
                value = weatherDisplay.humidityText,
                label = stringResource(R.string.fever_label_humidity),
                modifier = Modifier.fillMaxSize(),
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
private fun DetailsSection(weatherDisplay: WeatherDisplayData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AnimatedVisibility(
            visible = weatherDisplay.pressureText.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
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
        }
        AnimatedVisibility(
            visible = weatherDisplay.latitudeText.isNotBlank(),
            enter = fadeIn(animationSpec = tween(durationMillis = FadeDurationMs)),
            exit = fadeOut(animationSpec = tween(durationMillis = FadeDurationMs)),
        ) {
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
}

@Composable
private fun ForecastSection(forecast: List<DailyForecastDisplayData>) {
    Column {
        Text(
            text = stringResource(R.string.fever_forecast_header),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(12.dp))
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(forecast) { day ->
                    ForecastDayItem(day = day)
                }
            }
        }
    }
}

@Composable
private fun ForecastDayItem(day: DailyForecastDisplayData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AsyncImage(
            model = day.iconUrl,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = day.highText,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = day.lowText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// --- Preview Data ---

private val previewForecast = listOf(
    DailyForecastDisplayData(
        dayName = "Thu",
        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
        highText = "30°",
        lowText = "22°",
    ),
    DailyForecastDisplayData(
        dayName = "Fri",
        iconUrl = "https://openweathermap.org/img/wn/02d@2x.png",
        highText = "28°",
        lowText = "21°",
    ),
    DailyForecastDisplayData(
        dayName = "Sat",
        iconUrl = "https://openweathermap.org/img/wn/10d@2x.png",
        highText = "25°",
        lowText = "19°",
    ),
    DailyForecastDisplayData(
        dayName = "Sun",
        iconUrl = "https://openweathermap.org/img/wn/03d@2x.png",
        highText = "27°",
        lowText = "20°",
    ),
    DailyForecastDisplayData(
        dayName = "Mon",
        iconUrl = "https://openweathermap.org/img/wn/01d@2x.png",
        highText = "31°",
        lowText = "23°",
    ),
)

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
    forecast = previewForecast,
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

@Preview
@Composable
private fun ForecastSectionPreview() {
    FeverTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ForecastSection(forecast = previewForecast)
        }
    }
}
