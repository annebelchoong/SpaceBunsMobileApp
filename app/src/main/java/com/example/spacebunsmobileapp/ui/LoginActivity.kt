package com.example.spacebunsmobileapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.spacebunsmobileapp.MainActivity
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()

            //Validate email
            if (email.isEmpty()){
                binding.edtEmailLogin.error = "Email cannot be empty!"
                binding.edtEmailLogin.requestFocus()
                return@setOnClickListener
            }

            //email invalid
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtPasswordLogin.error = "Email is not Valid!"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            //Validate password
            if (password.isEmpty()){
                binding.edtPasswordLogin.error = "Password cannot be empty!"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            LoginFirebase(email,password)
        }
    }

    private fun LoginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Welcome $email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}