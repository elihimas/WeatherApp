package com.elihimas.weatherapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.elihimas.weatherapp.R
import com.elihimas.weatherapp.databinding.ActivityMainBinding
import com.elihimas.weatherapp.models.MainData
import com.elihimas.weatherapp.ui.adapters.ForecastItemsAdapter
import com.elihimas.weatherapp.ui.viewmodels.main.MainViewModel
import com.elihimas.weatherapp.ui.viewmodels.main.UiState
import com.elihimas.weatherapp.util.hide
import com.elihimas.weatherapp.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { ForecastItemsAdapter() }

    private val viewModel by viewModels<MainViewModel>()

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
            viewModel.createUiState().collect(::render)
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.RetryError -> renderRetryError()
            is UiState.FinalError -> renderFinalError()
            UiState.Loading -> renderLoading()
            is UiState.Success -> renderSuccess(uiState.data)
        }
    }

    private fun renderRetryError() {
        with(binding) {
            cityContainer.hide()
            itemsRecycler.hide()

            progress.show()
            tvStatus.show()
            tvStatus.setText(R.string.load_forecast_retry_error)
        }
    }

    private fun renderFinalError() {
        with(binding) {
            progress.hide()
            cityContainer.hide()
            itemsRecycler.hide()

            tvStatus.setText(R.string.load_forecast_final_error)
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.show()

            cityContainer.hide()
            itemsRecycler.hide()

            tvStatus.setText(R.string.load_forecast_loading)
        }
    }

    private fun renderSuccess(mainData: MainData) {
        with(binding) {
            progress.hide()

            cityContainer.show()
            itemsRecycler.show()

            tvStatus.setText(R.string.load_forecast_success)
            tvCity.text = mainData.weather.city

            val temperature = getString(R.string.row_item_temperature, mainData.weather.temperature)
            tvCityTemperature.text = temperature
        }

        adapter.items = mainData.forecast.forecastItems
    }
}

