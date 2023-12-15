package com.example.suitcase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.suitcase.databinding.FirstpageBinding

class Firstpage : AppCompatActivity() {
    private lateinit var binding: FirstpageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize binding
        binding = FirstpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup first page login button
        binding.firstLogBtn.setOnClickListener {

            val intent = Intent(this,Login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        //Setup first page sign up button
        binding.firstSignBtn.setOnClickListener {

            val intent = Intent(this,Sign_up_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }


    }
}