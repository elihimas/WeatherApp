package com.elihimas.weatherapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.elihimas.weather.data.model.WeatherData
import com.elihimas.weatherapp.databinding.ActivityMainBinding
import com.elihimas.weatherapp.ui.adapters.ForecastItemsAdapter
import com.elihimas.weatherapp.ui.viewmodels.ViewModelFactory
import com.elihimas.weatherapp.ui.viewmodels.main.MainViewModel
import com.elihimas.weatherapp.ui.viewmodels.main.UiState
import com.elihimas.weatherapp.util.hide
import com.elihimas.weatherapp.util.show
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { ForecastItemsAdapter() }

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        binding.itemsRecycler.adapter = adapter
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect(::render)
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            UiState.Loading -> renderLoading()
            is UiState.Success -> renderSuccess(uiState.data)
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.show()

            tvCity.hide()
            itemsRecycler.hide()
        }
    }

    private fun renderSuccess(weatherData: WeatherData) {
        with(binding) {
            progress.hide()

            tvCity.show()
            itemsRecycler.show()

            tvCity.text = weatherData.city
        }
        adapter.items = weatherData.forecastItems
    }
}

