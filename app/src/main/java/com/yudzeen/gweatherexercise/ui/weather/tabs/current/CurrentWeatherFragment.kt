package com.yudzeen.gweatherexercise.ui.weather.tabs.current

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import com.yudzeen.gweatherexercise.R
import com.yudzeen.gweatherexercise.common.Result
import com.yudzeen.gweatherexercise.databinding.FragmentCurrentWeatherBinding
import com.yudzeen.gweatherexercise.ui.weather.WeatherViewModel
import com.yudzeen.gweatherexercise.ui.weather.update
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels({ requireParentFragment() })

    private lateinit var binding: FragmentCurrentWeatherBinding

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineGranted || coarseGranted) {
            fetchCurrentLocation()
        } else {
            showFeatureDisabledPlaceholder()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeFetchCurrentWeatherResult()
        checkPermissionsAndFetchLocation()
    }

    private fun observeFetchCurrentWeatherResult() {
        viewModel.fetchCurrentWeatherResult.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Error -> {
                    binding.loadingIndicator.visibility = View.GONE
                    Toast.makeText(requireContext(), getString(R.string.fetch_current_weather_error), Toast.LENGTH_SHORT).show()
                }
                Result.Loading -> binding.loadingIndicator.visibility = View.VISIBLE
                is Result.Success -> {
                    Log.d("CurrentWeatherFragment", "observeFetchCurrentWeatherResult. Weather: ${it.data}")
                    binding.loadingIndicator.visibility = View.GONE
                    binding.currentWeatherView.root.visibility = View.VISIBLE
                    val weather = it.data
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

    private fun fetchCurrentWeather(location: Location) {
        viewModel.fetchCurrentWeather(location.latitude, location.longitude)
    }

    private fun checkPermissionsAndFetchLocation() {
        binding.loadingIndicator.isVisible = true
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                fetchCurrentLocation()
            }
            else -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showFeatureDisabledPlaceholder() {
        Snackbar.make(requireView(), "Please enable Location permission.", Snackbar.LENGTH_INDEFINITE).show()
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val locationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setDurationMillis(10000)
            .build()

        // Cancellation token used to clear memory if the task is canceled
        val cancellationTokenSource = CancellationTokenSource()
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.getCurrentLocation(locationRequest, cancellationTokenSource.token)
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("CurrentWeatherFragment", "fetchCurrentLocation: $location")
                    fetchCurrentWeather(location)
                } else {
                    Log.e("CurrentWeatherFragment", "Failed to retrieve location")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CurrentWeatherFragment", "fetchCurrentLocation: ", exception)
            }
    }

}