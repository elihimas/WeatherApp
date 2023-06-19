package com.elihimas.weatherapp.ui.viewmodels.main

import com.elihimas.weather.citiesrepository.CitiesRepository
import com.elihimas.weather.citiesrepository.City
import com.elihimas.weatherapp.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    fun `when repository send cities then should display the cities`() = runTest {
        // Arrange
        val city = "Recife"
        val expectedCities = listOf(City(city))
        val citiesRepository = mockk<CitiesRepository>()
        coEvery { citiesRepository.allCities() } returns flowOf(expectedCities)

        val viewModel = MainViewModel(citiesRepository)

        // Act
        val states = viewModel.uiState.take(2).toList()

        // Verify
        assertEquals(UiState.Loading, states[0])
        assertEquals(UiState.Success(expectedCities), states[1])
    }

    @Test
    fun `when repository continues to fail then should retry and then display the error`() =
        runTest {
            // Arrange
            val citiesRepository = mockk<CitiesRepository>()
            coEvery { citiesRepository.allCities() } coAnswers { throw IOException() }

            val viewModel = MainViewModel(citiesRepository)

            // Act
            val states = viewModel.uiState.take(2).toList()

            // Verify
            assertEquals(UiState.Loading, states[0])
            assertEquals(UiState.RetryError, states[1])
            assertEquals(UiState.FinalError, states[2])
        }
}