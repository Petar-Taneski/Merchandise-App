package com.example.merchandizecodedesk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.merchandizecodedesk.databinding.ActivityMainMenuBinding

class MainMenu : AppCompatActivity() {
    private lateinit var binding:ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}