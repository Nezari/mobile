package com.example.weatherapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.api.API_KEY
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.example.weatherapp.viewmodel.WeatherViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: WeatherAdapter
    private var city: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, WeatherViewModelFactory(application))[WeatherViewModel::class.java]

        adapter = WeatherAdapter({ weather ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("weather", weather)
            startActivity(intent)
        }, viewModel)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        viewModel.weather.observe(this, Observer {
            adapter.submitList(it)
            it.firstOrNull()?.let { weatherItem ->
                if (weatherItem.location != city) {
                    city = weatherItem.location
                    viewModel.updateWeather( API_KEY)
                }
            }
        })

        viewModel.progressBarVisibility.observe(this, Observer {
            binding.progressBar.visibility = it
        })

        binding.btnRefresh.setOnClickListener {
            val location = binding.etSearch.text.toString().trim()
            if (location.isNotEmpty()) {
                val apiKey = API_KEY
                binding.progressBar.visibility = View.VISIBLE
                viewModel.refreshWeather(location, apiKey)
                binding.etSearch.text.clear()
            }
        }
    }

    private val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val weather = adapter.currentList[position]
            viewModel.deleteWeather(weather)
        }
    }
}
