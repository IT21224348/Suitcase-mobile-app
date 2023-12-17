package com.example.suitcase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

        itemResultLauncher.launch(intent)

    }

    private val itemResultLauncher =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ){ result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK){
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val intent = result.data
                if (intent != null){
                    nodeid = intent.getStringExtra("Item_id").toString()
                    Log.d("Update_Delete_Item_page", "Node ID: $nodeid")
                }
                if (userId != null){
                    database = FirebaseDatabase.getInstance().getReference("Items")
                    database.child(userId.toString()).child(nodeid).get().addOnSuccessListener {
                        if (it.exists()){
                            binding.ItemUpdateName.setText(it.child("item_name").value.toString())
                            binding.ItemUpdatePrice.setText(it.child("item_price").value.toString())
                            binding.ItemUpdateDescription.setText(it.child("description").value.toString())
                            binding.ItemUpdateImageurl.setText(it.child("image_url").value.toString())
                            deleteItem(userId, nodeid)
                        }else{
                            Toast.makeText(this, "Item not found",Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    fun showlist(view: View){
        var i: Intent
        i = Intent(this,ItemList_page::class.java)
        itemResultLauncher.launch(i)
        Log.d("Update_Delete_Item_page", "showlist method called")
    }

    private fun deleteItem(userId: String, nodeid: String) {
        database = FirebaseDatabase.getInstance().getReference("Items")
        database.child(userId).child(nodeid).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Item Deletion Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
