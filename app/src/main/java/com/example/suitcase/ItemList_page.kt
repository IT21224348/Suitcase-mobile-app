package com.example.suitcase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.suitcase.Adapters.Item_Adapter
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.databinding.ActivityItemListPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ItemList_page : AppCompatActivity() {
    private lateinit var binding: ActivityItemListPageBinding
    private lateinit var ItemRV : RecyclerView
    private lateinit var ItemArrayList: ArrayList<Item_Model>
    private lateinit var db : DatabaseReference
    private val nodeList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Initialize the binding
        binding = ActivityItemListPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setup floating button for add item page
        binding.addItemBtn.setOnClickListener {
            val intent = Intent(this,Add_Iteam_page::class.java)
            intent.flags  = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)

        }


        ItemRV = binding.itemsRv
        ItemRV.layoutManager = LinearLayoutManager(this)
        ItemRV.hasFixedSize()
        ItemArrayList = arrayListOf<Item_Model>()
        db = FirebaseDatabase.getInstance().getReference().child("Items")
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                 if(snapshot.exists()){
                     for (itemsnapshot in snapshot.children){
                         val item = itemsnapshot.getValue(Item_Model::class.java)
                         println(itemsnapshot)
                         ItemArrayList.add(item!!)
                         nodeList.add(itemsnapshot.key.toString())
                     }
                     var adapter = Item_Adapter(ItemArrayList)
                     ItemRV.adapter = adapter
                 }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        // val options = FirebaseRecyclerOptions.Builder<Item_Model>()
        //     .setQuery(FirebaseDatabase.getInstance().getReference().child("Items"),Item_Model::class.java)
        //     .build()



    }
}