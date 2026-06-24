package com.yudzeen.gweatherexercise.ui.weather.tabs.current

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yudzeen.gweatherexercise.databinding.FragmentCurrentWeatherBinding
import com.yudzeen.gweatherexercise.ui.weather.WeatherViewModel
import com.yudzeen.gweatherexercise.ui.weather.update
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels({ requireParentFragment() })

    private lateinit var binding: FragmentCurrentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.currentWeather.observe(viewLifecycleOwner) {
            Log.d("CurrentWeatherFragment", "Current weather: $it")
            it?.let { weather ->
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

}