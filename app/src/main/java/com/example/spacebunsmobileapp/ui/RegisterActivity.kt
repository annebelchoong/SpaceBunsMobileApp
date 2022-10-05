package com.example.spacebunsmobileapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth
    lateinit var store: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()
            val phone = binding.edtPhoneRegister.text.toString()
            val name = binding.edtNameRegister.text.toString()
            val address = binding.edtAddressRegister.text.toString()

            //Validate email
            if (email.isEmpty()) {
                binding.edtEmailRegister.error = "Email cannot be empty!"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            //invalid email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmailRegister.error = "Email is not Valid!"
                binding.edtEmailRegister.requestFocus()
                return@setOnClickListener
            }

            //Validate password
            if (password.isEmpty()) {
                binding.edtPasswordRegister.error = "Password cannot be empty!"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            //Validate length of password
            if (password.length < 6) {
                binding.edtPasswordRegister.error = "Password must more than 6 character!"
                binding.edtPasswordRegister.requestFocus()
                return@setOnClickListener
            }

            RegisterFirebase(email, password)
        }
    }

    private fun RegisterFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Registration is Successful!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

}