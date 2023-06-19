package com.elihimas.weatherapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elihimas.weatherapp.R
import com.elihimas.weatherapp.databinding.FragmentCityWeatherBinding
import com.elihimas.weatherapp.ui.adapters.ForecastItemsAdapter
import com.elihimas.weatherapp.ui.viewmodels.city.CityData
import com.elihimas.weatherapp.ui.viewmodels.city.CityWeatherViewModel
import com.elihimas.weatherapp.ui.viewmodels.city.UiState
import com.elihimas.weatherapp.util.hide
import com.elihimas.weatherapp.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val ARG_CITY_NAME = "cityName"

@AndroidEntryPoint
class CityWeatherFragment : Fragment() {

    private lateinit var binding: FragmentCityWeatherBinding
    private val adapter by lazy { ForecastItemsAdapter() }

    private val viewModel by viewModels<CityWeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadCity()
    }

    private fun loadCity() {
        arguments?.let { bundle ->
            val cityName = bundle.getString(ARG_CITY_NAME)
            cityName?.let { city ->
                viewModel.loadCity(city)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        with(binding) {
            itemsRecycler.adapter = adapter
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
            is UiState.Success -> renderSuccess(uiState.data)
            is UiState.RetryError -> renderRetryError()
            is UiState.FinalError -> renderFinalError()
        }
    }

    private fun hideAllViews() {
        with(binding) {
            val allViews =
                listOf(
                    progress,
                    titleContainer,
                    tvCity,
                    tvCityTemperature,
                    itemsRecycler
                )
            allViews.forEach(View::hide)
        }
    }

    private fun renderLoading() {
        with(binding) {
            progress.show()

            tvStatus.setText(R.string.status_load_forecast_loading)
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

    private fun renderSuccess(cityData: CityData) {
        with(binding) {
            titleContainer.show()
            itemsRecycler.show()
            tvCityTemperature.show()
            tvCity.show()

            tvCity.text = cityData.weather.city
            val temperature = getString(R.string.row_item_temperature, cityData.weather.temperature)
            tvCityTemperature.text = temperature

            tvStatus.setText(R.string.status_load_forecast_success)
        }

        adapter.items = cityData.forecast.forecastItems
    }

    companion object {
        @JvmStatic
        fun newInstance(cityName: String) =
            CityWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CITY_NAME, cityName)
                }
            }
    }
}