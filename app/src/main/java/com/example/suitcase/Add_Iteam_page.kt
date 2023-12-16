package com.example.suitcase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityAddIteamPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Add_Iteam_page : AppCompatActivity() {

    private lateinit var binding : ActivityAddIteamPageBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initializing binding
        binding = ActivityAddIteamPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set a click listener on the "Cancel" button
        binding.ItemCancleBtn.setOnClickListener {
            //Clear the input fields when the "Cancel" button is clicked
            binding.itemNameInput.setText("")
            binding.itemPriceInput.setText("")
            binding.itemDescriptionInput.setText("")
            binding.itemImageUrlInput.setText("")
        }

        //Set a click listener on the "Add" button

        binding.ItemSubmitBtn.setOnClickListener {
            if (CheckAllFields()){

                val UserId  = FirebaseAuth.getInstance().currentUser?.uid

                val Item_Name        = binding.itemNameInput.text.toString()
                val Item_Price       = binding.itemPriceInput.text.toString()
                val Item_Description = binding.itemDescriptionInput.text.toString()
                val Item_ImageUrl    = binding.itemImageUrlInput.text.toString()

                database = FirebaseDatabase.getInstance().getReference("Items")
                val Item = Item_Model(Item_Name,Item_Price,Item_Description,Item_ImageUrl)

                if (UserId != null){
                    database.child(UserId).push().setValue(Item).addOnSuccessListener {

                        binding.itemNameInput.text.clear()
                        binding.itemPriceInput.text.clear()
                        binding.itemDescriptionInput.text.clear()
                        binding.itemImageUrlInput.text.clear()

                        Toast.makeText(this,"Item added Successfully",Toast.LENGTH_SHORT).show()

                    }.addOnFailureListener {
                        Toast.makeText(this,"Item adding is not successfull",Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }

//This function checks whether all fields are filled before proceeding with further actions.
private fun CheckAllFields():Boolean{

    val Item_Name        = binding.itemNameInput.text.toString().trim()
    val Iten_Price       = binding.itemPriceInput.text.toString().trim()
    val Item_Description = binding.itemDescriptionInput.toString().trim()
    val Item_ImageUrl    = binding.itemImageUrlInput.toString().trim()

    if (Item_Name.isEmpty()){
        binding.itemNameInput.error = "Please fill in the all required fields"
        return false
    }

    if(Iten_Price.isEmpty()){
        binding.itemPriceInput.error  = "Please fill in the all required fields"
        return false
    }

    if(Item_Description.isEmpty()){
        binding.itemDescriptionInput.error = "Please fill in the all required fields"
        return false
    }

    if (Item_ImageUrl.isEmpty()){
        binding.itemImageUrlInput.error = "Please fill in the all required fields"
    }

    //Verify completeness: Return true only if all fields are filled
    return true

}
}