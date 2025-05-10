package com.example.truthshellapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.truthshellapp.databinding.ActivityMainBinding // Import ViewBinding class

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initial setup, perhaps show the landing fragment
        if (savedInstanceState == null) {
            // TODO: Add logic to display the initial fragment (e.g., LandingFragment)
        }
    }
}

