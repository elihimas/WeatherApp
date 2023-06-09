package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.model.Weather
import com.elihimas.weather.data.repository.WeatherRepository
import com.elihimas.weatherapp.models.MainData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun shutDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when repository send weather info then should display the forecast`() = runTest {
        // Arrange
        val expectedForecastItems = createExpectedForecasts()
        val expectedWeather = Weather("Recife", 27.0)
        val expectedForecast = Forecast(expectedForecastItems)
        val weatherRepository = mockk<WeatherRepository>()
        every { weatherRepository.loadForecast() } returns flow { emit(expectedForecast) }
        every { weatherRepository.loadWeather() } returns flow { emit(expectedWeather) }
        val citiesRepository = mockk<CitiesRepository>()
        coEvery { citiesRepository.allCities() } returns emptyFlow()

        val viewModel = MainViewModel(weatherRepository, citiesRepository)

        // Act
        val states = viewModel.createUiState().take(2).toList()

        // Verify
        assertEquals(UiState.Loading, states[0])
        assertEquals(UiState.Success(MainData(expectedWeather, expectedForecast)), states[1])
    }

    private fun createExpectedForecasts() = listOf(
        Pair(28.0, 1686247200L),
        Pair(27.0, 1686322800L),
        Pair(29.0, 1686409200L)
    ).map { temperatureAndTime ->
        ForecastItem(
            temperatureAndTime.first,
            LocalDateTime.ofEpochSecond(
                temperatureAndTime.second,
                0,
                ZoneOffset.ofTotalSeconds(0)
            )
        )
    }

    @Test
    fun `when repository continues to fail then should retry and then display the error`() =
        runTest {
            // Arrange
            val weatherRepository = mockk<WeatherRepository>()
            val exceptionalFlow = flow<Forecast> { throw IOException() }
            every { weatherRepository.loadForecast() } returns exceptionalFlow
            every { weatherRepository.loadWeather() } returns emptyFlow()
            val citiesRepository = mockk<CitiesRepository>()
            coEvery { citiesRepository.allCities() } returns emptyFlow()

            val viewModel = MainViewModel(weatherRepository, citiesRepository)

            // Act
            val states = viewModel.createUiState().take(3).toList()

            // Verify
            assertEquals(UiState.Loading, states[0])
            assertEquals(UiState.RetryError, states[1])
            assertEquals(UiState.FinalError, states[2])
        }

    @Test
    fun `when repository fails once and recovers then should retry and then display the forecast`() =
        runTest {
            // Arrange
            val expectedForecastItems = createExpectedForecasts()
            val expectedWeather = Weather("Recife", 27.0)
            val expectedForecast = Forecast(expectedForecastItems)
            val weatherRepository = mockk<WeatherRepository>()
            var count = 0
            val exceptionalFlow = flow {
                delay(100)

                if (count == 0) {
                    count++
                    throw IOException()
                } else {
                    emit(expectedForecast)
                }
            }
            every { weatherRepository.loadForecast() } returns exceptionalFlow
            every { weatherRepository.loadWeather() } returns flow { emit(expectedWeather) }
            val citiesRepository = mockk<CitiesRepository>()
            coEvery { citiesRepository.allCities() } returns emptyFlow()

            val viewModel = MainViewModel(weatherRepository, citiesRepository)

            // Act
            val states = viewModel.createUiState().take(3).toList()

            // Verify
            assertEquals(UiState.Loading, states[0])
            assertEquals(UiState.RetryError, states[1])
            assertEquals(UiState.Success(MainData(expectedWeather, expectedForecast)), states[2])
        }
}