package com.elihimas.weatherapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.elihimas.weather.data.model.Forecast
import com.elihimas.weatherapp.R
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
            is UiState.RetryError -> renderRetryError()
            is UiState.FinalError -> renderFinalError()
            UiState.Loading -> renderLoading()
            is UiState.Success -> renderSuccess(uiState.data)
        }
    }

    private fun renderRetryError() {
        with(binding) {
            itemsRecycler.hide()

            progress.show()
            tvStatus.show()
            tvStatus.setText(R.string.load_forecast_retry_error)
        }
    }

    private fun renderFinalError() {
        with(binding) {
            progress.hide()
            itemsRecycler.hide()

            tvStatus.show()
            tvStatus.setText(R.string.load_forecast_final_error)
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.show()

            tvStatus.hide()
            itemsRecycler.hide()
        }
    }

    private fun renderSuccess(forecast: Forecast) {
        with(binding) {
            progress.hide()

            tvStatus.show()
            itemsRecycler.show()

            tvStatus.setText(R.string.load_forecast_success)
        }
        adapter.items = forecast.forecastItems
    }
}

