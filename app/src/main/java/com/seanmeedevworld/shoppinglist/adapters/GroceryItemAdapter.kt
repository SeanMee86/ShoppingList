package com.seanmeedevworld.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.seanmeedevworld.shoppinglist.R
import com.seanmeedevworld.shoppinglist.models.GroceryItem

class GroceryItemAdapter(options: FirebaseRecyclerOptions<GroceryItem>, private val query: DatabaseReference) :
    FirebaseRecyclerAdapter<GroceryItem, GroceryItemAdapter.GroceryItemViewHolder>(options) {

    class GroceryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvGroceryItemName)
        val quantity: TextView = itemView.findViewById(R.id.tvGroceryItemQuantity)
        val gotten: CheckBox = itemView.findViewById(R.id.cbGotten)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_firebase_grocery, parent, false)
        return GroceryItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: GroceryItemViewHolder,
        position: Int,
        model: GroceryItem
    ) {
        holder.name.text = model.name
        holder.gotten.isChecked = model.gotten
        holder.quantity.text = model.quantity.toString()

        holder.gotten.setOnClickListener {
            val groceryKey = getRef(position).key
            this.query
                .child(groceryKey.toString())
                .child("gotten")
                .setValue(holder.gotten.isChecked)
        }
    }

}