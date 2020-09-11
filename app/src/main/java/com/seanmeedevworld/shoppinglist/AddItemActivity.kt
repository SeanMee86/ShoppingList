package com.seanmeedevworld.shoppinglist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.seanmeedevworld.shoppinglist.models.GroceryItem
import kotlinx.android.synthetic.main.activity_add_item.*

class AddItemActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
        mAuth = FirebaseAuth.getInstance()

        btnAddGrocery.setOnClickListener {
            addGrocery()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_item_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.save_item -> addGrocery()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addGrocery() {
        val name = etGrocery.text.toString()
        val quantity = etQuantity.text.toString()
        val notebookRef = FirebaseFirestore.getInstance()
        if(name.isEmpty()) {
            Toast.makeText(this, "Please enter an item", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            if (quantity.isEmpty()) {
                notebookRef
                    .collection("User")
                    .document(mAuth.currentUser?.uid.toString())
                    .collection("GroceryList")
                    .add(GroceryItem(name))
            } else {
                notebookRef
                    .collection("User")
                    .document(mAuth.currentUser?.uid.toString())
                    .collection("GroceryList")
                    .add(GroceryItem(name, quantity.toInt()))
            }
            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show()
        } catch (e: ApiException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        etGrocery.text.clear()
        etQuantity.text.clear()
    }
}