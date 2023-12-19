package com.example.suitcase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityUpdateDeleteItemPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class Update_Delete_Item_page : AppCompatActivity() {
    var nodeid = ""
    private lateinit var binding: ActivityUpdateDeleteItemPageBinding
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState:  Bundle?) {
        super.onCreate(savedInstanceState)
        // Initializing binding
        binding = ActivityUpdateDeleteItemPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        if (intent.hasExtra("Item_id")) {
            val itemID = intent.getStringExtra("Item_id")
            // Now, use itemID to retrieve additional details and display them
            if (itemID != null) {
                displayItemDetails(itemID)
                binding.ItemDeleteBtn.setOnClickListener {

                    val builder = AlertDialog.Builder(this@Update_Delete_Item_page)
                    builder.setTitle("Are you sure you want to delete the item ?")
                    builder.setPositiveButton("Yes") { _, _ ->
                        deleteItem(itemID)
                    }
                    builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

                    builder.show()

                }

                binding.ItemUpdateBtn.setOnClickListener {
                    updateItem(itemID)
                }
            }
        }



    }

    private fun updateItem(itemID: String) {

        val Item_name = binding.ItemUpdateName.text.toString()
        val Item_price  = binding.ItemUpdatePrice.text.toString()
        val Item_description = binding.ItemUpdateDescription.text.toString()
        val Item_iamege  = binding.ItemUpdateImageurl.text.toString()

        database = FirebaseDatabase.getInstance().getReference("Items")
        val Item = Item_Model(Item_name,Item_price,Item_description,Item_iamege)
        val UserId = FirebaseAuth.getInstance().currentUser?.uid
        if (UserId != null){

            database.child(UserId).child(itemID).setValue(Item).addOnSuccessListener {
                setResult(RESULT_OK)
                finish()
            }

        }

    }

    private fun deleteItem(itemID: String) {
        val UserId = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("Items")
        if (UserId != null){
            database.child(UserId).child(itemID).removeValue().addOnSuccessListener {
                setResult(RESULT_OK)
               /*
                val intent = Intent(this,ItemList_page::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or  Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
*/
                finish()
            }

        }
    }

    private fun displayItemDetails(s: String) {

        val UserId = FirebaseAuth.getInstance().currentUser?.uid
        if (UserId!= null){

            database = FirebaseDatabase.getInstance().getReference("Items").child(UserId)
            database.child(s).get().addOnSuccessListener {
                if (it.exists()) {
                    binding.ItemUpdateName.setText(it.child("item_name").value.toString())
                    binding.ItemUpdatePrice.setText(it.child("item_price").value.toString())
                    binding.ItemUpdateDescription.setText(it.child("description").value.toString())
                    binding.ItemUpdateImageurl.setText(it.child("image_url").value.toString())
                }  else{
                    Toast.makeText(this,"Item not Found",Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
            }

        }

    }


}
