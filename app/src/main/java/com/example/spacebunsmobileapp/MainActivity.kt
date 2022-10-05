package com.example.spacebunsmobileapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spacebunsmobileapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

var RC_SIGN_IN = 1

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nav by lazy { supportFragmentManager.findFragmentById(R.id.host)!!.findNavController() }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // move from one fragment to another using the bottom nav
        binding.bottomNavBar.setupWithNavController(nav)

    }
    private fun fetchdata() {
        TODO("Not yet implemented")

    }

    override fun onSupportNavigateUp(): Boolean {
        return nav.navigateUp() || super.onSupportNavigateUp()
    }

}