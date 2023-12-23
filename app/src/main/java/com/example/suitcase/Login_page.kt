package com.example.suitcase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.suitcase.databinding.ActivityLoginPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay


class Login_page : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initializing binding
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup login page sign_up button
        binding.loginSignupBtn.setOnClickListener {
            val intent = Intent(this,Sign_up_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        //Get a instance of the firebase authentication
        auth = Firebase.auth
        //Attach a click listner to the "Log in" button using view binding
        binding.loginBtn.setOnClickListener {
            //Perform login logic here
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()
            if (CheckAllFields()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Toast.makeText(this,"Successfully signed in",Toast.LENGTH_SHORT).show()

                        //Navigate to item list page
                        val intent = Intent(this, ItemList_page::class.java)
                        startActivity(intent)
                    }else{
                        Log.e("error: ", task.exception.toString())
                        Toast.makeText(this,"Check your login credentials",Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }

    }

//This function checks whether all fields are filled before proceeding with further actions.
private fun  CheckAllFields():Boolean{

    val email = binding.loginEmail.text.toString().trim()
    val password = binding.loginPassword.text.toString().trim()

    //Determine if there are any empty fields
    if (email.isEmpty()){
        binding.loginEmail.error = "Please enter your email"
        return false
    }

    if (password.isEmpty()){
        binding.loginPassword.error = "Please enter your password"
        return false
    }

    //Check email format
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        binding.loginEmail.error = "We're sorry but the email address you provided is not valid"
    }

    //Verify completeness: Return true only if all fields are filled
    return true
}
}