package com.example.suitcase.Adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.suitcase.DataClass.Item_Model
import com.example.suitcase.R
import de.hdodenhof.circleimageview.CircleImageView

class Item_Adapter(private var ItemList: ArrayList<Item_Model>):RecyclerView.Adapter<Item_Adapter.ItemHolder>() {

    interface OnPurchasedButtonClickListener{
        fun onPurchasedButtonClick(item:Item_Model)
    }

    interface OnSMSButtonClickListener{
        fun onSMSButtonClick(item:Item_Model)
    }


    var onPurchasedButtonClickListener: OnPurchasedButtonClickListener? =null
    var onSMSButtonClickListener: OnSMSButtonClickListener? = null
    inner class ItemHolder(ItemView: View):RecyclerView.ViewHolder(ItemView){

        val Item_Name    : TextView          = ItemView.findViewById(R.id.Item_name)
        val Item_Price   : TextView          = ItemView.findViewById(R.id.Item_price)
        val Item_Image   : CircleImageView   = ItemView.findViewById(R.id.item_image)
        val Sms_btn      : ImageButton       = ItemView.findViewById(R.id.sms_btn)
        val purchase_btn : ImageButton       = ItemView.findViewById(R.id.purchased_btn)

        init {
            Sms_btn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = ItemList[position]
                    onSMSButtonClickListener?.onSMSButtonClick(currentItem)
                }
            }
            purchase_btn.setOnClickListener{
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val currentItem = ItemList[position]
                    onPurchasedButtonClickListener?.onPurchasedButtonClick(currentItem)
                }
            }


        }
        private fun sendSms(item: Item_Model?) {
            item?.let {
                // Check for SMS permission
                if (ContextCompat.checkSelfPermission(itemView.context, android.Manifest.permission.SEND_SMS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    // Permission not granted, request it
                    ActivityCompat.requestPermissions(
                        itemView.context as AppCompatActivity,
                        arrayOf(android.Manifest.permission.SEND_SMS),
                        SMS_PERMISSION_REQUEST_CODE
                    )
                } else {
                    // Permission already granted, proceed with SMS sending
                    val smsIntent = Intent(Intent.ACTION_SENDTO)
                    smsIntent.data = Uri.parse("smsto:")  // This ensures it opens in the default SMS app
                    smsIntent.putExtra("sms_body", "Item Name: ${it.item_name}\nItem Price: ${it.item_price}")
                    itemView.context.startActivity(smsIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item_Adapter.ItemHolder {
         val ItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
         return ItemHolder(ItemView)
    }

    override fun onBindViewHolder(holder: Item_Adapter.ItemHolder, position: Int) {
         val Current_Item = ItemList[position]
         holder.Item_Name.setText(Current_Item.item_name.toString())
         holder.Item_Price.text = Current_Item.item_price.toString()
         Glide.with(holder.Item_Image.context)
             .load(Current_Item.image_url)
             .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
             . error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
             .into(holder.Item_Image);

    }

    override fun getItemCount(): Int {
       return ItemList.size

    }
    companion object {
        private const val SMS_PERMISSION_REQUEST_CODE = 123
    }
}


