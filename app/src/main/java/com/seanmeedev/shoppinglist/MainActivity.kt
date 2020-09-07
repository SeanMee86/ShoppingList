package com.seanmeedev.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth
    lateinit var database : FirebaseDatabase
    lateinit var recyclerView: RecyclerView

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

        val mAdapter = GroceryItemAdapter(options, query)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        rvGroceries.layoutManager = layoutManager
        rvGroceries.adapter = mAdapter
        mAdapter.startListening()

        btnAddGrocery.setOnClickListener {
            addGrocery(query)
        }

        signOutBtn.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
    
    fun addGrocery(query: DatabaseReference) {
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