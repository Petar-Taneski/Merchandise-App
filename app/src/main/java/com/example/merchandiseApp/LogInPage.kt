package com.example.merchandiseApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.merchandiseApp.databinding.ActivityLogInPageBinding

class LogInPage : AppCompatActivity() {
    private lateinit var binding: ActivityLogInPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            var username = binding.etUsername.text.toString()
            var password = binding.etPassword.text.toString()


            if(username=="admin" && password=="adminpass"){
                Intent(this,MainMenu::class.java).also{
                    Toast.makeText(applicationContext,"Welcome $username.", Toast.LENGTH_SHORT).show()
                    startActivity(it)
                }
            }else{
                Toast.makeText(applicationContext,"Access denied, wrond credentials.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}