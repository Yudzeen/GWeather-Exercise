package com.yudzeen.gweatherexercise.ui.weather

import androidx.core.content.ContextCompat
import com.yudzeen.gweatherexercise.R
import com.yudzeen.gweatherexercise.databinding.ViewWeatherBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun ViewWeatherBinding.update(
    city: String,
    country: String,
    temperatureCelsius: Double,
    condition: String,
    sunriseTime: Long,
    sunsetTime: Long
) {
    val context = root.context
    textLocation.text = context.getString(R.string.city_country, city, country)
    textTemperature.text = context.getString(R.string.temp_in_c, temperatureCelsius.toInt())

    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    textSunrise.text = context.getString(R.string.sunrise_time, formatter.format(Date(sunriseTime)))
    textSunset.text = context.getString(R.string.sunset_time, formatter.format(Date(sunsetTime)))

    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    val icon = when (condition) {
        "Clear" -> if (currentHour in 6..17) R.drawable.ic_sun else R.drawable.ic_moon
        "Clouds" -> R.drawable.ic_cloud
        "Rain" -> R.drawable.ic_rain
        else -> null
    }

    if (icon != null) {
        val drawable = ContextCompat.getDrawable(context, icon)
        val tintedDrawable = drawable?.mutate()?.apply {
            setTint(ContextCompat.getColor(context, android.R.color.darker_gray))
        }
        weatherIcon.setImageDrawable(tintedDrawable)
    }
}