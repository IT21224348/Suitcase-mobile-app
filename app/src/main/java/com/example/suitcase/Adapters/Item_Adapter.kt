package com.example.suitcase.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.R
import de.hdodenhof.circleimageview.CircleImageView

class Item_Adapter(private var ItemList: ArrayList<Item_Model>):RecyclerView.Adapter<Item_Adapter.ItemHolder>() {


    class ItemHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        val Item_Name    : TextView          = ItemView.findViewById(R.id.Item_name)
        val Item_Price   : TextView          = ItemView.findViewById(R.id.Item_price)
        val Item_Image   : CircleImageView   = ItemView.findViewById(R.id.item_image)
        val Sms_btn      : ImageButton       = ItemView.findViewById(R.id.sms_btn)
        val purchase_btn : ImageButton       = ItemView.findViewById(R.id.purchased_btn)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item_Adapter.ItemHolder {
         val ItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
         return ItemHolder(ItemView)
    }

    override fun onBindViewHolder(holder: Item_Adapter.ItemHolder, position: Int) {
         val Current_Item = ItemList[position]
         holder.Item_Name.setText(Current_Item.Item_name.toString())
         holder.Item_Price.text = Current_Item.Item_price.toString()
         Glide.with(holder.Item_Image.context)
             .load(Current_Item.Image_url)
             .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
             . error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
             .into(holder.Item_Image);

    }

    override fun getItemCount(): Int {
       return ItemList.size

    }
}


