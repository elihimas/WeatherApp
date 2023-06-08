package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.data.model.Forecast
import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weather.data.repository.WeatherRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
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
        val expectedTemperatures = listOf(28.0, 27.0, 29.0).map(::ForecastItem)
        val expectedForecast = Forecast(expectedTemperatures)
        val repository = mockk<WeatherRepository>()
        every { repository.loadForecast() } returns flow { emit(expectedForecast) }

        val viewModel = MainViewModel(repository)

        // Act
        val states = viewModel.uiState.take(2).toList()

        // Verify
        assertEquals(UiState.Loading, states[0])
        assertEquals(UiState.Success(expectedForecast), states[1])
    }

    @Test
    fun `when repository continues to fail then should retry and then display the error`() =
        runTest {
            // Arrange
            val repository = mockk<WeatherRepository>()
            val exceptionalFlow = flow<Forecast> { throw IOException() }
            every { repository.loadForecast() } returns exceptionalFlow

            val viewModel = MainViewModel(repository)

            // Act
            val states = viewModel.uiState.take(3).toList()

            // Verify
            assertEquals(UiState.Loading, states[0])
            assertEquals(UiState.RetryError, states[1])
            assertEquals(UiState.FinalError, states[2])
        }

    @Test
    fun `when repository fails once and recovers then should retry and then display the forecast`() =
        runTest {
            // Arrange
            val expectedTemperatures = listOf(28.0, 27.0, 29.0).map(::ForecastItem)
            val expectedForecast = Forecast(expectedTemperatures)
            val repository = mockk<WeatherRepository>()
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
            every { repository.loadForecast() } returns exceptionalFlow
            val viewModel = MainViewModel(repository)

            // Act
            val states = viewModel.uiState.take(3).toList()

            // Verify
            assertEquals(UiState.Loading, states[0])
            assertEquals(UiState.RetryError, states[1])
            assertEquals(UiState.Success(expectedForecast), states[2])
        }
}