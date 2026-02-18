package com.sls.handbook.feature.fever

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.model.Weather
import com.sls.handbook.feature.fever.theme.LocalFeverColors
import com.theapache64.rebugger.Rebugger
import java.util.Locale

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
        when (uiState) {
            is FeverUiState.Loading -> LoadingContent()
            is FeverUiState.Error -> ErrorContent(message = uiState.message, onRetry = onRefresh)
            is FeverUiState.Success -> WeatherContent(weather = uiState.weather)
        }

        if (uiState !is FeverUiState.Loading) {
            FloatingActionButton(
                onClick = onRefresh,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp),
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh location")
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Could not load weather",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text("Try another location")
        }
    }
}

@Composable
private fun WeatherContent(weather: Weather) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp),
    ) {
        HeroSection(weather = weather)
        Spacer(modifier = Modifier.height(24.dp))
        LocationHeader(weather = weather)
        Spacer(modifier = Modifier.height(12.dp))
        WeatherDescription(weather = weather)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Details",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(12.dp))
        DetailsSection(weather = weather)
        Spacer(modifier = Modifier.height(96.dp))
    }
}

@Composable
private fun HeroSection(weather: Weather) {
    val feverColors = LocalFeverColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        WeatherIconCard(
            weather = weather,
            modifier = Modifier.weight(1.2f).fillMaxHeight(),
        )
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatPill(
                icon = Icons.Default.Thermostat,
                iconBackgroundColor = feverColors.iconOrange,
                value = "H:${weather.tempMax.toInt()}° L:${weather.tempMin.toInt()}°",
                label = "High / Low",
                modifier = Modifier.weight(1f),
            )
            StatPill(
                icon = Icons.Default.Air,
                iconBackgroundColor = feverColors.iconBlue,
                value = "${weather.windSpeed} m/s",
                label = "Wind",
                modifier = Modifier.weight(1f),
            )
            StatPill(
                icon = Icons.Default.WaterDrop,
                iconBackgroundColor = feverColors.iconTeal,
                value = "${weather.humidity}%",
                label = "Humidity",
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
            Text(
                text = value,
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
private fun LocationHeader(weather: Weather) {
    val locationName = if (weather.cityName.isNotBlank()) {
        if (weather.country.isNotBlank()) "${weather.cityName}, ${weather.country}" else weather.cityName
    } else {
        "Unknown Location"
    }
    Text(
        text = locationName,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun WeatherDescription(weather: Weather) {
    val description = weather.description.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
    Column {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Feels like ${weather.feelsLike.toInt()}°C",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun DetailsSection(weather: Weather) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = "Pressure",
                value = "${weather.pressure} hPa",
            )
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = "Visibility",
                value = if (weather.visibility < 1000) {
                    "${weather.visibility} m"
                } else {
                    "${weather.visibility / 1000} km"
                },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = "Latitude",
                value = String.format(Locale.US, "%.4f", weather.latitude),
            )
            GlassDetailCard(
                modifier = Modifier.weight(1f),
                label = "Longitude",
                value = String.format(Locale.US, "%.4f", weather.longitude),
            )
        }
    }
}
