package com.elihimas.weatherapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elihimas.weatherapp.R
import com.elihimas.weatherapp.databinding.ActivityMainBinding
import com.elihimas.weatherapp.models.MainData
import com.elihimas.weatherapp.ui.adapters.ForecastItemsAdapter
import com.elihimas.weatherapp.ui.addcity.AddCityActivity
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
        with(binding) {
            itemsRecycler.adapter = adapter
            btAddCity.setOnClickListener {
                viewModel.onAddClicked()
                startActivity(Intent(this@MainActivity, AddCityActivity::class.java))
            }
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::render)
            }
        }
    }

    private fun render(uiState: UiState) {
        hideAllViews()

        when (uiState) {
            UiState.Loading -> renderLoading()
            is UiState.NoCitiesRegistered -> renderAddCity()
            is UiState.Success -> renderSuccess(uiState.data)
            is UiState.RetryError -> renderRetryError()
            is UiState.FinalError -> renderFinalError()
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.show()

            tvStatus.setText(R.string.status_load_forecast_loading)
        }
    }

    private fun renderAddCity() {
        with(binding) {
            tvEmptyCitiesMessage.show()
            tvStatus.setText(R.string.status_add_city)
        }
    }

    private fun renderRetryError() {
        with(binding) {
            progress.show()
            tvStatus.setText(R.string.status_load_forecast_retry_error)
        }
    }

    private fun renderFinalError() {
        with(binding) {
            tvStatus.setText(R.string.status_load_forecast_final_error)
        }
    }

    private fun renderSuccess(mainData: MainData) {
        with(binding) {
            titleContainer.show()
            itemsRecycler.show()
            tvCityTemperature.show()
            tvCity.show()

            tvCity.text = mainData.weather.city
            val temperature = getString(R.string.row_item_temperature, mainData.weather.temperature)
            tvCityTemperature.text = temperature

            tvStatus.setText(R.string.status_load_forecast_success)
        }

        adapter.items = mainData.forecast.forecastItems
    }

    private fun hideAllViews() {
        with(binding) {
            val allViews =
                listOf(
                    progress,
                    titleContainer,
                    tvCity,
                    tvCityTemperature,
                    itemsRecycler,
                    tvEmptyCitiesMessage
                )
            allViews.forEach(View::hide)
        }

    }
}

