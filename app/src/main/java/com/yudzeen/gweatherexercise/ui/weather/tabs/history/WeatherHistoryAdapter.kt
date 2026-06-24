package com.yudzeen.gweatherexercise.ui.weather.tabs.history

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yudzeen.gweatherexercise.databinding.ListItemWeatherBinding
import com.yudzeen.gweatherexercise.domain.weather.Weather
import com.yudzeen.gweatherexercise.ui.weather.update

class WeatherHistoryAdapter: RecyclerView.Adapter<WeatherHistoryAdapter.ViewHolder>() {

    private val weatherList: MutableList<Weather> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ListItemWeatherBinding.inflate(
                android.view.LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(weatherList[position])
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(users: List<Weather>) {
        weatherList.clear()
        weatherList.addAll(users)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ListItemWeatherBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bindItem(weather: Weather) {
            binding.currentWeatherView.update(
                weather.city,
                weather.country,
                weather.temperature,
                weather.condition,
                weather.sunrise,
                weather.sunset
            )
        }

    }
}