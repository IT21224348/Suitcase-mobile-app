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

class Purchased_Adapter(private var PurchasedList: ArrayList<Item_Model>) : RecyclerView.Adapter<Purchased_Adapter.PurchasedHolder>() {

    interface OnPurchasedDeleteButtonClickListener {
        fun onPurchasedDeleteButtonClick(item: Item_Model)
    }

    var onPurchasedDeleteButtonClickListener: OnPurchasedDeleteButtonClickListener? = null

    inner class PurchasedHolder(purchasedview: View) : RecyclerView.ViewHolder(purchasedview) {

        val purchased_item_name: TextView = purchasedview.findViewById(R.id.Item_name)
        val purchased_item_price: TextView = purchasedview.findViewById(R.id.Item_price)
        val purchased_item_image: CircleImageView = purchasedview.findViewById(R.id.item_image)
        val purchased_item_delete: ImageButton = purchasedview.findViewById(R.id.purchased_delete_btn)

        init {
            purchased_item_delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val currentitem = PurchasedList[position]
                    onPurchasedDeleteButtonClickListener?.onPurchasedDeleteButtonClick(currentitem)


                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Purchased_Adapter.PurchasedHolder {
        var purchasedview = LayoutInflater.from(parent.context).inflate(R.layout.purchased_layout,parent,false)
        return PurchasedHolder(purchasedview)
    }

    override fun onBindViewHolder(holder: Purchased_Adapter.PurchasedHolder, position: Int) {
        val currentpurchasedItem = PurchasedList[position]
        holder.purchased_item_name.setText(currentpurchasedItem.item_name.toString())
        holder.purchased_item_price.setText(currentpurchasedItem.item_price).toString()
        Glide.with(holder.purchased_item_image.context)
            .load(currentpurchasedItem.image_url)
            .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
            . error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
            .into(holder.purchased_item_image);


    }

    override fun getItemCount(): Int {
        return PurchasedList.size
    }


}