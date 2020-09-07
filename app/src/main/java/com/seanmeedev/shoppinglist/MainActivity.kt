package com.seanmeedev.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.seanmeedev.shoppinglist.adapters.GroceryItemAdapter
import com.seanmeedev.shoppinglist.models.GroceryItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: GroceryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        recyclerView = rvGroceries
        rvGroceries.setHasFixedSize(true)

        val query = database
            .reference
            .child("User")
            .child(mAuth.currentUser?.uid.toString())
            .child("GroceryList")

        val options = FirebaseRecyclerOptions.Builder<GroceryItem>()
            .setQuery(query, GroceryItem::class.java)
            .build()

        mAdapter = GroceryItemAdapter(options, query)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        rvGroceries.layoutManager = layoutManager
        rvGroceries.adapter = mAdapter
        swipeToDelete(recyclerView)

        btnAddGrocery.setOnClickListener {
            addGrocery(query)
        }

        signOutBtn.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mAdapter.getRef(viewHolder.adapterPosition).removeValue()
            }
        }).attachToRecyclerView(recyclerView)
    }
    
    private fun addGrocery(query: DatabaseReference) {
        val name = etGrocery.text.toString()
        val quantity = etQuantity.text.toString()
        if(name.isEmpty()) {
            Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show()
            return
        }
        if (quantity.isEmpty()) {
            query.push()
                .setValue(GroceryItem(name))
        } else {
            query.push()
                .setValue(GroceryItem(name, quantity.toInt()))
        }
        etGrocery.text.clear()
        etQuantity.text.clear()
    }
}