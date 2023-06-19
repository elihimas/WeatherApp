package com.elihimas.weatherapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elihimas.weather.citiesrepository.City
import com.elihimas.weatherapp.R
import com.elihimas.weatherapp.databinding.ActivityMainBinding
import com.elihimas.weatherapp.ui.adapters.CitiesAdapter
import com.elihimas.weatherapp.ui.addcity.AddCityActivity
import com.elihimas.weatherapp.ui.viewmodels.main.UiState
import com.elihimas.weatherapp.util.hide
import com.elihimas.weatherapp.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter by lazy { CitiesAdapter(this) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        with(binding) {
            citiesPager.adapter = adapter

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
            is UiState.Success -> renderSuccess(uiState.cities)
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

    private fun renderSuccess(cities: List<City>) {
        with(binding) {
            tvStatus.setText(R.string.status_load_forecast_success)
            citiesPager.show()
        }

        adapter.cities = cities
    }

    private fun hideAllViews() {
        with(binding) {
            val allViews =
                listOf(
                    progress,
                    citiesPager,
                    tvEmptyCitiesMessage
                )
            allViews.forEach(View::hide)
        }
    }
}

