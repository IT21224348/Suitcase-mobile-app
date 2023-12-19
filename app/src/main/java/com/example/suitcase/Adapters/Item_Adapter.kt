package com.example.suitcase.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageButton
import android.widget.TextView
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

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener{
        fun OnItemClick(position: Int)
    }

    fun setOnItemClickListener(listener:OnItemClickListener){
        mListener = listener
    }

    var onPurchasedButtonClickListener: OnPurchasedButtonClickListener? =null
    var onSMSButtonClickListener: OnSMSButtonClickListener? = null
    inner class ItemHolder(ItemView: View, listener:OnItemClickListener):RecyclerView.ViewHolder(ItemView){

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

            ItemView.setOnClickListener {
                listener.OnItemClick(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item_Adapter.ItemHolder {
         val ItemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
         return ItemHolder(ItemView,mListener)
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


