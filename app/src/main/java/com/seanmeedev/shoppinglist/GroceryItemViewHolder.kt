package com.seanmeedev.shoppinglist

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val name: TextView = itemView.findViewById(R.id.tvGroceryItemName)
    val quantity: TextView = itemView.findViewById(R.id.tvGroceryItemQuantity)
    val gotten: CheckBox = itemView.findViewById(R.id.cbGotten)
}