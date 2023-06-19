package com.elihimas.weatherapp.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elihimas.weather.citiesrepository.City
import com.elihimas.weatherapp.ui.main.CityWeatherFragment

class CitiesAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var cities = listOf<City>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        val city = cities[position]

        return CityWeatherFragment.newInstance(city.name)
    }

}

