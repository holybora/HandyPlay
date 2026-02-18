package com.sls.handbook.feature.fever

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.sls.handbook.core.model.Weather
import com.theapache64.rebugger.Rebugger
import java.util.Locale

@Composable
fun FeverScreen(
    uiState: FeverUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Rebugger(composableName = "FeverScreen", trackMap = mapOf("uiState" to uiState))

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is FeverUiState.Loading -> LoadingContent()
            is FeverUiState.Error -> ErrorContent(message = uiState.message, onRetry = onRefresh)
            is FeverUiState.Success -> WeatherContent(weather = uiState.weather)
        }

        if (uiState !is FeverUiState.Loading) {
            FloatingActionButton(
                onClick = onRefresh,
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
        CircularProgressIndicator()
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
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LocationHeader(weather = weather)

        Spacer(modifier = Modifier.height(16.dp))

        TemperatureSection(weather = weather)

        Spacer(modifier = Modifier.height(16.dp))

        WeatherDetailsGrid(weather = weather)

        Spacer(modifier = Modifier.height(16.dp))

        CoordinatesCard(weather = weather)

        // Bottom padding so FAB doesn't overlap content
        Spacer(modifier = Modifier.height(88.dp))
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
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun TemperatureSection(weather: Weather) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (weather.icon.isNotBlank()) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${weather.icon}@2x.png",
                    contentDescription = weather.description,
                    modifier = Modifier.size(80.dp),
                )
            }

            Text(
                text = "${weather.temperature.toInt()}°C",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Light,
            )

            Text(
                text = weather.description.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    text = "H: ${weather.tempMax.toInt()}°",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "L: ${weather.tempMin.toInt()}°",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = "Feels ${weather.feelsLike.toInt()}°",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun WeatherDetailsGrid(weather: Weather) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DetailCard(
                modifier = Modifier.weight(1f),
                label = "Humidity",
                value = "${weather.humidity}%",
            )
            DetailCard(
                modifier = Modifier.weight(1f),
                label = "Wind",
                value = "${weather.windSpeed} m/s",
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DetailCard(
                modifier = Modifier.weight(1f),
                label = "Pressure",
                value = "${weather.pressure} hPa",
            )
            DetailCard(
                modifier = Modifier.weight(1f),
                label = "Visibility",
                value = "${weather.visibility / 1000} km",
            )
        }
    }
}

@Composable
private fun DetailCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier) {
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
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun CoordinatesCard(weather: Weather) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Location",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Lat ${String.format(Locale.US, "%.4f", weather.latitude)}  " +
                    "Lon ${String.format(Locale.US, "%.4f", weather.longitude)}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
