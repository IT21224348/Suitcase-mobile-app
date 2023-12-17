package com.example.suitcase.DataClass

import java.io.Serializable

data class Item_Model(val item_name:String? = null,
                      val item_price:String? = null,
                      val description:String? = null,
                      val image_url:String? = null): Serializable
