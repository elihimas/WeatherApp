package com.elihimas.weatherapp.ui.addcity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.elihimas.weather.data.model.Weather
import com.elihimas.weatherapp.R
import com.elihimas.weatherapp.databinding.ActivityAddCityBinding
import com.elihimas.weatherapp.util.hide
import com.elihimas.weatherapp.util.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCityActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddCityBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<AddCityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initViewModel()
    }

    private fun initViews() {
        with(binding) {
            btFindCity.setOnClickListener {
                val cityName = etCityName.text.toString()
                viewModel.findCity(cityName)
            }

            btAddCity.setOnClickListener {
                val cityName = tvCity.text.toString()
                viewModel.addCity(cityName)
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

    private fun render(state: UiState) {
        hideAllViews()

        when (state) {
            UiState.Idle -> {}
            UiState.Loading -> renderLoading()
            UiState.CityNotFound -> renderNotFound()
            is UiState.CityFound -> renderFoundCityWeather(state.weather)
            UiState.CityAdded -> renderCityAdded()
        }
    }

    private fun renderCityAdded() {
        Toast.makeText(this, R.string.message_city_added, Toast.LENGTH_LONG).show()

        with(binding) {
            resultContainer.hide()

            etCityName.isEnabled = true
            btFindCity.isEnabled = true
        }
    }

    private fun renderFoundCityWeather(weather: Weather) {
        with(binding) {
            resultContainer.show()

            etCityName.isEnabled = true
            btFindCity.isEnabled = true

            etCityName.setText("")

            tvCity.show()
            tvTemperature.show()

            tvCity.text = weather.city
            tvTemperature.text = getString(R.string.row_item_temperature, weather.temperature)
        }
    }

    private fun renderLoading() {
        with(binding) {
            etCityName.isEnabled = false
            btFindCity.isEnabled = false

            progress.show()
        }
    }

    private fun renderNotFound() {
        with(binding) {
            etCityName.isEnabled = true
            btFindCity.isEnabled = true

            tvNotFound.show()
        }
    }

    private fun hideAllViews() {
        with(binding) {
            val allViews = listOf(resultContainer, progress, tvNotFound)

            allViews.forEach(View::hide)
        }
    }
}