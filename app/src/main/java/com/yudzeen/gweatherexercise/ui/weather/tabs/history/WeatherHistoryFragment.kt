package com.yudzeen.gweatherexercise.ui.weather.tabs.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.yudzeen.gweatherexercise.R
import com.yudzeen.gweatherexercise.databinding.FragmentWeatherHistoryBinding
import com.yudzeen.gweatherexercise.ui.weather.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherHistoryFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels({ requireParentFragment() })

    private lateinit var binding: FragmentWeatherHistoryBinding
    private lateinit var adapter: WeatherHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = WeatherHistoryAdapter()
        binding.recyclerView.adapter = adapter
        viewModel.weatherHistory.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}