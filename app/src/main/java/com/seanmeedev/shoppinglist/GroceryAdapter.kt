package com.seanmeedev.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_grocery.view.*

class GroceryAdapter(
    private var groceries: List<Grocery>
) : RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder>() {
    inner class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_grocery, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.itemView.apply {
            tvGroceryItem.text = groceries[position].name
        }
    }

    override fun getItemCount(): Int {
        return groceries.size
    }
}