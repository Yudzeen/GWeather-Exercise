package com.yudzeen.gweatherexercise.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.yudzeen.gweatherexercise.R
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.databinding.FragmentWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()

    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    viewModel.logout()
                    true
                }
                else -> false
            }
        }

        val adapter = WeatherPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.current)
                1 -> getString(R.string.history)
                else -> throw IllegalStateException("Invalid tab position")
            }
        }.attach()

        observeLogoutResult()
    }

    private fun observeLogoutResult() {
        viewModel.logoutResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    binding.loadingIndicator.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.logout_error), Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> binding.loadingIndicator.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.loadingIndicator.visibility = View.GONE
                    findNavController().navigate(WeatherFragmentDirections.actionWeatherFragmentToLoginFragment())
                }
            }
        }
    }
}