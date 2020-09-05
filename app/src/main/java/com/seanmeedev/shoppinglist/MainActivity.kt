package com.seanmeedev.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        val groceryList = mutableListOf<Grocery>()

        val adapter = GroceryAdapter(groceryList)
        rvGroceries.adapter = adapter
        rvGroceries.layoutManager = LinearLayoutManager(this)

        btnAddGrocery.setOnClickListener {
            val name = etGrocery.text.toString()
            groceryList.add(Grocery(name, 0))
            adapter.notifyItemInserted(groceryList.size - 1)
            etGrocery.text.clear()
        }

        signOutBtn.setOnClickListener {
            mAuth.signOut()
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }
    }
}