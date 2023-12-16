package com.example.suitcase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitcase.Adapters.Purchased_Adapter
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityPurchasedListPageBinding
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
}