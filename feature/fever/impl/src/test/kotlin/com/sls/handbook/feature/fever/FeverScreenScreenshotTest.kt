package com.sls.handbook.feature.fever

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sls.handbook.core.screenshottesting.ScreenshotTestActivity
import com.sls.handbook.core.screenshottesting.captureScreenshot
import com.sls.handbook.feature.fever.theme.FeverTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class FeverScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    private val previewForecast = listOf(
        DailyForecastDisplayData(
            dayName = "Thu",
            iconUrl = "01d@2x.png",
            highText = "30°",
            lowText = "22°",
        ),
        DailyForecastDisplayData(
            dayName = "Fri",
            iconUrl = "02d@2x.png",
            highText = "28°",
            lowText = "21°",
        ),
        DailyForecastDisplayData(
            dayName = "Sat",
            iconUrl = "10d@2x.png",
            highText = "25°",
            lowText = "19°",
        ),
        DailyForecastDisplayData(
            dayName = "Sun",
            iconUrl = "03d@2x.png",
            highText = "27°",
            lowText = "20°",
        ),
        DailyForecastDisplayData(
            dayName = "Mon",
            iconUrl = "01d@2x.png",
            highText = "31°",
            lowText = "23°",
        ),
    )

    private val previewWeatherDisplay = WeatherDisplayData(
        temperatureText = "32°C",
        iconUrl = "03d@4x.png",
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
        fiveDaysForecast = previewForecast,
        hourlyForecasts = listOf(
            HourlyDisplayData(
                timeText = "Now",
                iconUrl = "03d@2x.png",
                temperatureText = "32°",
                popText = "10%",
            ),
            HourlyDisplayData(
                timeText = "14:00",
                iconUrl = "02d@2x.png",
                temperatureText = "33°",
                popText = "5%",
            ),
            HourlyDisplayData(
                timeText = "15:00",
                iconUrl = "01d@2x.png",
                temperatureText = "34°",
                popText = "0%",
            ),
            HourlyDisplayData(
                timeText = "16:00",
                iconUrl = "10d@2x.png",
                temperatureText = "31°",
                popText = "40%",
            ),
            HourlyDisplayData(
                timeText = "17:00",
                iconUrl = "03d@2x.png",
                temperatureText = "30°",
                popText = "20%",
            ),
        ),
    )

    @Test
    fun feverScreen_loading() {
        composeTestRule.captureScreenshot("FeverScreen_Loading") {
            FeverTheme {
                FeverScreen(uiState = FeverUiState.Loading, onEvent = {})
            }
        }
    }

    @Test
    fun feverScreen_success() {
        composeTestRule.captureScreenshot("FeverScreen_Success") {
            FeverTheme {
                FeverScreen(uiState = FeverUiState.Success(previewWeatherDisplay), onEvent = {})
            }
        }
    }

    @Test
    fun feverScreen_error() {
        composeTestRule.captureScreenshot("FeverScreen_Error") {
            FeverTheme {
                FeverScreen(
                    uiState = FeverUiState.Error("Unable to determine location"),
                    onEvent = {},
                )
            }
        }
    }
}
