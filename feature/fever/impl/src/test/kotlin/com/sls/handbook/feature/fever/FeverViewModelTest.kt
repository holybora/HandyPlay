package com.sls.handbook.feature.fever

import app.cash.turbine.test
import com.sls.handbook.core.domain.usecase.GenerateRandomCoordinatesUseCase
import com.sls.handbook.core.domain.usecase.GetCurrentWeatherUseCase
import com.sls.handbook.core.domain.usecase.GetFiveDayForecastUseCase
import com.sls.handbook.core.domain.usecase.GetForecastDataUseCase
import com.sls.handbook.core.domain.usecase.GetTodayHourlyForecastUseCase
import com.sls.handbook.core.model.Coordinates
import com.sls.handbook.core.model.ForecastData
import com.sls.handbook.core.model.Weather
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeverViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val stringResolver: StringResolver = mockk()
    private val getCurrentWeather: GetCurrentWeatherUseCase = mockk()
    private val getForecastData: GetForecastDataUseCase = mockk()
    private val getFiveDayForecast: GetFiveDayForecastUseCase = mockk()
    private val getTodayHourlyForecast: GetTodayHourlyForecastUseCase = mockk()
    private val generateRandomCoordinates: GenerateRandomCoordinatesUseCase = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { generateRandomCoordinates() } returns Coordinates(10.0, 20.0)
        coEvery { getCurrentWeather(any(), any(), any()) } returns testWeather
        coEvery { getForecastData(any(), any(), any()) } returns testForecastData
        every { getFiveDayForecast(any()) } returns emptyList()
        every { getTodayHourlyForecast(any()) } returns emptyList()
        every { stringResolver.getString(any(), *anyVararg()) } returns "test"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state is Loading`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        assertEquals(FeverUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `loads weather on init and transitions to Success`() = runTest(testDispatcher) {
        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(FeverUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertTrue(awaitItem() is FeverUiState.Success)
        }
    }

    @Test
    fun `onEvent Refresh triggers reload after Success`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.uiState.test {
            assertTrue(awaitItem() is FeverUiState.Success)

            viewModel.onEvent(FeverEvent.Refresh)
            assertEquals(FeverUiState.Loading, awaitItem())

            advanceUntilIdle()
            assertTrue(awaitItem() is FeverUiState.Success)
        }
    }

    @Test
    fun `onEvent Refresh is dropped while loading`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle() // complete init load

        viewModel.onEvent(FeverEvent.Refresh) // fires immediately
        viewModel.onEvent(FeverEvent.Refresh) // dropped — still Loading
        viewModel.onEvent(FeverEvent.Refresh) // dropped — still Loading
        advanceUntilIdle()

        // init + one refresh = exactly 2 calls
        coVerify(exactly = 2) { getCurrentWeather(any(), any(), any()) }
    }

    @Test
    fun `onEvent Refresh accepted again after load completes`() = runTest(testDispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle() // init load

        viewModel.onEvent(FeverEvent.Refresh)
        advanceUntilIdle() // first refresh completes

        viewModel.onEvent(FeverEvent.Refresh)
        advanceUntilIdle() // second refresh completes

        // init + 2 refreshes = exactly 3 calls
        coVerify(exactly = 3) { getCurrentWeather(any(), any(), any()) }
    }

    @Test
    fun `emits Error when use case throws`() = runTest(testDispatcher) {
        coEvery {
            getCurrentWeather(any(), any(), any())
        } throws RuntimeException("Network error")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(FeverUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertTrue(awaitItem() is FeverUiState.Error)
        }
    }

    @Test
    fun `onEvent Refresh works after Error state`() = runTest(testDispatcher) {
        coEvery {
            getCurrentWeather(any(), any(), any())
        } throws RuntimeException("fail")

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is FeverUiState.Error)

        coEvery {
            getCurrentWeather(any(), any(), any())
        } returns testWeather

        viewModel.onEvent(FeverEvent.Refresh)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value is FeverUiState.Success)
    }

    private fun createViewModel() = FeverViewModel(
        stringResolver = stringResolver,
        getCurrentWeather = getCurrentWeather,
        getForecastData = getForecastData,
        getFiveDayForecast = getFiveDayForecast,
        getTodayHourlyForecast = getTodayHourlyForecast,
        generateRandomCoordinates = generateRandomCoordinates,
    )

    private companion object {
        val testWeather = Weather(
            cityName = "Test City",
            country = "TC",
            latitude = 10.0,
            longitude = 20.0,
            temperature = 25.0,
            feelsLike = 27.0,
            tempMin = 22.0,
            tempMax = 28.0,
            humidity = 65,
            pressure = 1013,
            description = "clear sky",
            icon = "01d",
            windSpeed = 3.5,
            visibility = 10000,
        )
        val testForecastData = ForecastData(
            items = emptyList(),
            timezoneOffsetSeconds = 0,
        )
    }
}
