package com.example.suitcase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitcase.Adapters.Purchased_Adapter
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityPurchasedListPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PurchasedList_page : AppCompatActivity() {

    private lateinit var binding: ActivityPurchasedListPageBinding
    private lateinit var Purchasedrv: RecyclerView
    private lateinit var ItemArrayList: ArrayList<Item_Model>
    private lateinit var db: DatabaseReference
    private val nodeList = ArrayList<String>()
    private lateinit var  bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initializing the binding
        binding = ActivityPurchasedListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Purchasedrv = binding.purchasedRv
        Purchasedrv.layoutManager = LinearLayoutManager(this)
        Purchasedrv.hasFixedSize()
        ItemArrayList = arrayListOf()
        db = FirebaseDatabase.getInstance().getReference().child("purchased_list")
        val userId = FirebaseAuth.getInstance().currentUser?.uid


        //bottom navigation
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    val intent = Intent(this, ItemList_page::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
//                    val intent = Intent(this, PurchasedList_page::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.info -> {
                    val intent = Intent(this, AboutActivityPage::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            true
        }

        bottomNavigationView.selectedItemId = R.id.list

        if (userId != null) {
            val userItemsRef = db.child(userId)

            userItemsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        ItemArrayList.clear()

                        for (itemsnapshot in snapshot.children) {
                            val item = itemsnapshot.getValue(Item_Model::class.java)
                            println(itemsnapshot)
                            item?.let {
                                ItemArrayList.add(it)
                                nodeList.add(itemsnapshot.key.toString())
                            }
                        }

                        var adapter =Purchased_Adapter(ItemArrayList)
                        adapter.onPurchasedDeleteButtonClickListener =
                            object : Purchased_Adapter.OnPurchasedDeleteButtonClickListener{
                                override fun onPurchasedDeleteButtonClick(item: Item_Model){
                                    val builder = AlertDialog.Builder(this@PurchasedList_page)
                                    builder.setTitle("Are you sure you want to delete the item '${item.item_name}'?")
                                    builder.setPositiveButton("Yes") { _, _ ->
                                        deleteItemToPurchasedList(item, nodeList[ItemArrayList.indexOf(item)])
                                    }
                                    builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

                                    builder.show()


                                }
                                
                            }
                        Purchasedrv.adapter = adapter

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

    private fun deleteItemToPurchasedList(item: Item_Model, s: String) {
        val purchasedListRef = FirebaseDatabase.getInstance().getReference("purchased_list")
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            // Add the item to purchased_list using the same key as in item_list
            purchasedListRef.child(userId).child(s).removeValue()
                .addOnSuccessListener {


                    recreate()
                    Toast.makeText(this, "Item Deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle failure
                    Toast.makeText(this, "Failed to purchase item", Toast.LENGTH_SHORT).show()
                }
        }

    }
}