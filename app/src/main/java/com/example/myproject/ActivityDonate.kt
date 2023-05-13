package com.example.myproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.myproject.databinding.ActivityDonateBinding
import com.google.firebase.database.DatabaseReference

class ActivityDonate : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

        val foodButton: Button = findViewById(R.id.btnfood)
        val goodsButton: Button = findViewById(R.id.btngoods)
        val serviceButton: Button = findViewById(R.id.btnservice)


        val costBreakfast = intent.getDoubleExtra("costBreakfast",0.0)
        val costLunch = intent.getDoubleExtra("costLunch",0.0)
        val costDinner = intent.getDoubleExtra("costDinner",0.0)
        val count = intent.getIntExtra("count",0)
        val organization = intent.getStringExtra("organization")


        foodButton.setOnClickListener {
            // Set background color of clicked button to light blue
            foodButton.setBackgroundColor(resources.getColor(R.color.light_blue))

            // Set background color of all other buttons to white
            goodsButton.setBackgroundColor(resources.getColor(android.R.color.white))
            serviceButton.setBackgroundColor(resources.getColor(android.R.color.white))

            val bundle = Bundle()
            val fragmentFood = FoodFragment()

            bundle.putDouble("costBreakfast", costBreakfast)
            bundle.putDouble("costLunch", costLunch)
            bundle.putDouble("costDinner", costDinner)
            bundle.putInt("count", count)
            bundle.putString("organization", organization)
            fragmentFood.arguments = bundle

            // Show the FoodFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView5, fragmentFood)
                .commit()
        }

        goodsButton.setOnClickListener {
            // Set background color of clicked button to light blue
            goodsButton.setBackgroundColor(resources.getColor(R.color.light_blue))

            // Set background color of all other buttons to white
            foodButton.setBackgroundColor(resources.getColor(android.R.color.white))
            serviceButton.setBackgroundColor(resources.getColor(android.R.color.white))

            // Show the GoodsFragment
            val fragmentGoods = GoodsFragment()
            val bundle = Bundle()

            bundle.putString("organization", organization)
            fragmentGoods.arguments = bundle
            supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView5, fragmentGoods)
            .commit()
        }

        serviceButton.setOnClickListener {
            // Set background color of clicked button to light blue
            serviceButton.setBackgroundColor(resources.getColor(R.color.light_blue))

            // Set background color of all other buttons to white
            foodButton.setBackgroundColor(resources.getColor(android.R.color.white))
            goodsButton.setBackgroundColor(resources.getColor(android.R.color.white))

            // Show the ServiceFragment
            val fragmentService = ServiceFragment()
            val bundle = Bundle()

            bundle.putString("organization", organization)
            fragmentService.arguments = bundle
            supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView5, fragmentService)
            .commit()
        }
    }
}
