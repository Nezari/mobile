package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityDetailBinding
import com.example.weatherapp.db.Weather
import com.example.weatherapp.forecast.ForecastAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: WeatherViewModel by viewModels()

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val weather: Weather = intent.getParcelableExtra("weather")!!
        displayWeatherDetails(weather)

        binding.deleteButton.setOnClickListener { v: View ->
            val popupMenu = PopupMenu(this, v)
            popupMenu.inflate(R.menu.detail_menu)

            popupMenu.setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.item1 ->  { true }
                    R.id.item_delete -> {
                        viewModel.deleteWeather(weather)
                        finish()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }

        viewModel.getForecast(weather.location).observe(this) {
            binding.forecastRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = ForecastAdapter(emptyList())
            }

            viewModel.getForecast(weather.location).observe(this) { forecastList ->
                (binding.forecastRecyclerView.adapter as ForecastAdapter).updateList(forecastList)
            }
        }

        changeImage(weather)
    }

    private fun changeImage(weather: Weather) {
        val condition = weather.main
        when(condition){
            "Clouds" ->{
                binding.root.setBackgroundResource(R.drawable.cloud)
                binding.lottieAnimation.setAnimation(R.raw.cloud)
            }
            "Rain" ->{
                binding.root.setBackgroundResource(R.drawable.rain)
                binding.lottieAnimation.setAnimation(R.raw.rain)
            }
            "Clear" ->{
                binding.root.setBackgroundResource(R.drawable.day1)
                binding.lottieAnimation.setAnimation(R.raw.shiningsun)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayWeatherDetails(weather: Weather) {
        binding.apply {
            locationTextView.text = weather.location
            descriptionTextView.text = weather.description.capitalize(Locale.ROOT)
            temperatureTextView.text = "${weather.temperature}°C"
            feelLike.text = "${weather.feel_like}°C"
            visiblity.text = "${weather.visibilty}m"
            updatedAt.text = SimpleDateFormat("yyyy-MM-dd hh-mm a", Locale.getDefault()).format(Date(weather.updatedAt * 1000))
            wind.text = weather.windSpeed.toString()
            pressure.text = "${weather.pressure} hPa"
            humidity.text = weather.humidity.toString()
        }
    }
}
