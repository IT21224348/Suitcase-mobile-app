package com.example.suitcase

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.suitcase.databinding.ActivityOpenBinding

class Open_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenBinding
    private val DELAY_TIME = 5000L // 5 seconds in milliseconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initializing binding
        binding = ActivityOpenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed({
            startLoginActivity()
            finish() // Optional: Close the open screen to prevent going back to it
        }, DELAY_TIME)

    }

    private fun startLoginActivity() {
        val intent = Intent(this, login_page_start::class.java)
        startActivity(intent)
    }
}