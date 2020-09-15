package com.seanmeedevworld.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.seanmeedevworld.shoppinglist.adapters.GroceryItemFSAdapter
import com.seanmeedevworld.shoppinglist.models.GroceryItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var db : FirebaseFirestore
    private lateinit var groceryItemRef: CollectionReference
    private lateinit var adapter : GroceryItemFSAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        groceryItemRef =
            db.collection("User")
                .document(mAuth.currentUser?.uid.toString())
                .collection("GroceryList")

        addNoteBtn.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        groceryItemRef.addSnapshotListener{_, e ->
            if(e != null){
                return@addSnapshotListener
            }
            adapter.notifyDataSetChanged()
        }

        setUpRecyclerView()
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
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun setUpRecyclerView() {
        val query = groceryItemRef

        val options = FirestoreRecyclerOptions.Builder<GroceryItem>()
            .setQuery(query, GroceryItem::class.java)
            .build()

        adapter = GroceryItemFSAdapter(options, query)

        recyclerView  = rvGroceries
        rvGroceries.setHasFixedSize(true)
        rvGroceries.layoutManager = LinearLayoutManager(this)
        rvGroceries.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(rvGroceries)
    }

    private fun signOut() {
        mAuth.signOut()
        startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}