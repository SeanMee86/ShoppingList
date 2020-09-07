package com.seanmeedev.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.seanmeedev.shoppinglist.adapters.GroceryItemAdapter
import com.seanmeedev.shoppinglist.models.GroceryItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var recyclerView: RecyclerView
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

        addNoteBtn.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logoutMenuItem -> signOut()
        }
        return super.onOptionsItemSelected(item)
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
    private fun signOut() {
        mAuth.signOut()
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}