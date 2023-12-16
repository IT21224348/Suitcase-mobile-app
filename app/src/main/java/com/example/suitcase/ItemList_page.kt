package com.example.suitcase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitcase.Adapters.Item_Adapter
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityItemListPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ItemList_page : AppCompatActivity() {
    private lateinit var binding: ActivityItemListPageBinding
    private lateinit var ItemRV: RecyclerView
    private lateinit var ItemArrayList: ArrayList<Item_Model>
    private lateinit var db: DatabaseReference
    private val nodeList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the binding
        binding = ActivityItemListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup floating button for add item page
        binding.addItemBtn.setOnClickListener {
            val intent = Intent(this, Add_Iteam_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        //Setup purchased item button for item page
        binding.itemlistPurchasedBtn.setOnClickListener {
            val intent = Intent(this,PurchasedList_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        ItemRV = binding.itemsRv
        ItemRV.layoutManager = LinearLayoutManager(this)
        ItemRV.hasFixedSize()
        ItemArrayList = arrayListOf()
        db = FirebaseDatabase.getInstance().getReference().child("Items")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val userItemsRef = db.child(userId)

            userItemsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        ItemArrayList.clear()

                        for (itemsnapshot in snapshot.children) {
                            val item = itemsnapshot.getValue(Item_Model::class.java)
                            println(itemsnapshot)
                            ItemArrayList.add(item!!)
                            nodeList.add(itemsnapshot.key.toString())

                        }

                        var adapter = Item_Adapter(ItemArrayList)
                        adapter.onPurchasedButtonClickListener =
                            object : Item_Adapter.OnPurchasedButtonClickListener {
                                override fun onPurchasedButtonClick(item: Item_Model) {
                                    // Handle the purchased button click
                                    moveItemToPurchasedList(item, nodeList[ItemArrayList.indexOf(item)])
                                }
                            }

                        ItemRV.adapter = adapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled if needed
                    // In this example, logging the error message
                    Log.e("ItemList_page", "DatabaseError: ${error.message}")
                }
            })
        } else {
            // Handle the case where the user is not authenticated
            // For example, redirect to the login screen
            val intent = Intent(this, Login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun moveItemToPurchasedList(item: Item_Model, itemKey: String) {
        val purchasedListRef = FirebaseDatabase.getInstance().getReference("purchased_list")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Add the item to purchased_list using the same key as in item_list
            purchasedListRef.child(userId).child(itemKey).setValue(item)
                .addOnSuccessListener {
                    // Successfully moved to purchased_list
                    // You may want to remove the item from item_list as well
                    // Update UI or perform any other actions.
                    db.child(userId).child(itemKey).removeValue()
                    // Reload the activity to refresh the UI
                    finish()
                    startActivity(intent)

                    Toast.makeText(this, "Item purchased successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle failure
                    Toast.makeText(this, "Failed to purchase item", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
