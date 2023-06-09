package com.elihimas.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.elihimas.weather.data.model.ForecastItem
import com.elihimas.weatherapp.R
import com.elihimas.weatherapp.databinding.RowForecastItemBinding
import java.time.format.DateTimeFormatter

class ForecastItemsAdapter : RecyclerView.Adapter<ForecastItemsAdapter.Holder>() {

    class Holder(private val binding: RowForecastItemBinding) : ViewHolder(binding.root) {

        fun bind(item: ForecastItem) {
            val temperatureText =
                binding.root.resources.getString(R.string.row_item_temperature, item.temperature)
            val dayText = formatter.format(item.date)

            binding.tvTemperature.text = temperatureText
            binding.tvDay.text = dayText
        }
    }

    var items = listOf<ForecastItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowForecastItemBinding.inflate(layoutInflater, parent, false)

        return Holder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    companion object {
        // TODO: use local pattern
        val formatter = DateTimeFormatter.ofPattern("dd/MM")
    }
}
