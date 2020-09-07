package com.seanmeedevworld.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.seanmeedevworld.shoppinglist.MainActivity
import com.seanmeedevworld.shoppinglist.R
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment(R.layout.fragment_login), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signInBtn.setOnClickListener(this)
    }

    private fun validateUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        if(email.isEmpty()){
            Toast.makeText(requireActivity(), "Please enter an email", Toast.LENGTH_SHORT).show()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(requireActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show()
            return
        }
        if(password.isEmpty()){
            Toast.makeText(requireActivity(), "Please enter a password", Toast.LENGTH_SHORT).show()
            return
        }
        loginUser(email, password)
    }

    private fun loginUser(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()){
                if(it.isSuccessful) {
                    Log.d(TAG, "loginWithEmail: successful")
                    Toast.makeText(requireActivity(), "Login Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    activity?.finish()
                } else {
                    Log.w(TAG, "loginWithEmail: failed", it.exception)
                    Toast.makeText(requireActivity(), it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.signInBtn -> validateUser()
        }
    }

    companion object {
        val TAG = "FirebaseSignin"
    }
}