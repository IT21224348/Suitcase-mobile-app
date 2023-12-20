package com.example.suitcase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.suitcase.databinding.ActivityLoginPageStartBinding

class login_page_start : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize binding
        binding = ActivityLoginPageStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup first page login button
        binding.startLoginBtn.setOnClickListener {
            val intent = Intent(this,Login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        //Setup first page sign up button
        binding.regText.setOnClickListener {
            val intent = Intent(this,Sign_up_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }
}