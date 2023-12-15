package com.example.suitcase

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.suitcase.databinding.ActivitySignUpPageBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Sign_up_page : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initializing binding
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get a instance of the firebase authentication
        auth = Firebase.auth

        //Attach a click listener to the "Sign up" button using view binding
        binding.signupBtn.setOnClickListener {
            //perform signup logic here
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPwd.text.toString()
            if (CheckAllFields()){
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener {task ->
                        if (task.isSuccessful){
                            val signInMethods = task.result?.signInMethods
                            if(signInMethods.isNullOrEmpty()) {
                                auth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener {signUpTask ->
                                        if (signUpTask.isSuccessful){
                                            //Account creation success
                                            Toast.makeText(this,"Account created successfully",Toast.LENGTH_SHORT).show()
                                            startActivity(Intent(this,Login_page::class.java))
                                            finish()

                                            //Send verification email
                                            val user = Firebase.auth.currentUser

                                            user!!.sendEmailVerification()
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Log.d(TAG, "Email sent.")

                                                    }
                                                }
                                        }else{
                                            //Account creation not success
                                            Log.e("error: ", signUpTask.exception.toString())
                                            Toast.makeText(this,"Account not created successfully",Toast.LENGTH_SHORT).show()
                                            finish()

                                        }
                                    }
                            }else{
                                //Email is already registered. Display error message
                                Toast.makeText(this,"This email is already registered. Please use a different email",Toast.LENGTH_LONG).show()

                            }
                        }else{
                            //Error occurred while checking email, display error message
                            Toast.makeText(this,"Error:${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }

//This function checks whether all fields are filled before proceeding with further actions.
private fun CheckAllFields():Boolean{
    //Retrieve the text content from input fields.
    val email = binding.signupEmail.text.toString().trim()
    val password = binding.signupPwd.text.toString().trim()
    val cnf_password = binding.signupPwdConfirm.text.toString().trim()

    //Determine if there are any empty fields.
    if (email.isEmpty()){
        binding.signupEmail.error = "Please enter your email"
        return false
    }

    if (password.isEmpty()){
        binding.signupPwd.error = "Please enter password"
        return false
    }

    if (cnf_password.isEmpty()){
        binding.signupPwdConfirm.error = "Please enter password"
        return false
    }

    //Verify if the password and confirm password fields match.
    if (password != cnf_password){
        binding.signupPwdConfirm.error = "Passwords do not match"
        return false
    }

    // Validate that the password has a minimum of 6 characters.
    if(password.length < 6){
        binding.signupPwd.error = "Password should be at least 6 characters"
        return false
    }

    //Check email format
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        binding.signupEmail.error = "Your email is not in correct format"
        return false
    }


    return true
  }


}