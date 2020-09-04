package com.seanmeedev.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        mAuth = FirebaseAuth.getInstance()

        if(mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val emailLoginFragment = LoginFragment()
        val emailRegisterFragment = RegisterFragment()
        val googleSignOnFragment = GoogleSignOnFragment()

        supportFragmentManager.beginTransaction().apply {
            add(R.id.flLayout, emailLoginFragment)
            commit()
        }

        registerBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flLayout, emailRegisterFragment)
                commit()
            }
        }

        loginBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flLayout, emailLoginFragment)
                commit()
            }
        }

        googleSignInBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flLayout, googleSignOnFragment)
                commit()
            }
        }

    }
}