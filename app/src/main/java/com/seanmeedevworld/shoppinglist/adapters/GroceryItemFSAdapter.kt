package com.seanmeedevworld.shoppinglist.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.seanmeedevworld.shoppinglist.R
import com.seanmeedevworld.shoppinglist.models.GroceryItem

class GroceryItemFSAdapter(options: FirestoreRecyclerOptions<GroceryItem>, private val query : CollectionReference) :
    FirestoreRecyclerAdapter<GroceryItem, GroceryItemFSAdapter.GroceryItemHolder>(options) {

    class GroceryItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvGroceryItemName)
        val quantity: TextView = itemView.findViewById(R.id.tvGroceryItemQuantity)
        val gotten: CheckBox = itemView.findViewById(R.id.cbGotten)
    }

    fun deleteItem(position: Int) {
        snapshots.getSnapshot(position).reference.delete()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroceryItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_firebase_grocery, parent, false)
        return GroceryItemHolder(view)
    }

    override fun onBindViewHolder(
        holder: GroceryItemHolder,
        position: Int,
        model: GroceryItem
    ) {
        holder.name.text = model.name
        holder.gotten.isChecked = model.gotten
        holder.quantity.text = model.quantity.toString()

        holder.gotten.setOnClickListener {
            val groceryKey = snapshots.getSnapshot(position).reference.id
            println("*******************************")
            Log.d("CheckBoxId", "The checkbox id is: $groceryKey")
            println("*******************************")
            val checkBox = hashMapOf(
                "gotten" to holder.gotten.isChecked
            )
            this.query.document(groceryKey).update(checkBox as Map<String, Any>)
        }
    }

}