package com.example.merchandiseApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.merchandiseApp.databinding.ActivityMainMenuBinding


class MainMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNewEntry.setOnClickListener {
            Intent(this, MerchandiseForm::class.java).also {
                startActivity(it)
            }
        }

        binding.btnBrowse.setOnClickListener{
            Intent(this, BrowseFiles::class.java).also {
                startActivity(it)
            }
        }

    }
}