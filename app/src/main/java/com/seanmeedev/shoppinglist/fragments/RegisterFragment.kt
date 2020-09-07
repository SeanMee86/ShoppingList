package com.seanmeedev.shoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.seanmeedev.shoppinglist.MainActivity
import com.seanmeedev.shoppinglist.R
import kotlinx.android.synthetic.main.fragment_register.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment(R.layout.fragment_register), View.OnClickListener {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpBtn.setOnClickListener(this)
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
        signupUser(email, password)
    }

    private fun signupUser(email: String, password: String){
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                Log.d(TAG, "createUserWithEmail: success")
                Toast.makeText(requireActivity(), "Successful Registration", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                activity?.finish()
            } else {
                Log.w(TAG, "createUserWithEmail: failed", it.exception)
                Toast.makeText(requireActivity(), it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(p0: View) {
        when(p0.id){
            R.id.signUpBtn -> validateUser()
        }
    }

    companion object {
        val TAG = "FirebaseRegistration"
    }
}