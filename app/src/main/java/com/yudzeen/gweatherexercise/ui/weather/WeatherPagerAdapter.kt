package com.yudzeen.gweatherexercise.ui.weather

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yudzeen.gweatherexercise.ui.weather.tabs.current.CurrentWeatherFragment
import com.yudzeen.gweatherexercise.ui.weather.tabs.history.WeatherHistoryFragment

class WeatherPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentWeatherFragment()
            1 -> WeatherHistoryFragment()
            else -> throw IllegalStateException("Invalid tab position")
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}