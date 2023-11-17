package com.hfad.beeradviser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val findBeer = findViewById<Button>(R.id.find_beer)
        findBeer.setOnClickListener() {
            val brands = findViewById<TextView>(R.id.brands)
            val beerColor = findViewById<Spinner>(R.id.beer_color)
            val color = beerColor.selectedItem.toString()

            brands.text = "Beer color is $color"
        }
    }
}