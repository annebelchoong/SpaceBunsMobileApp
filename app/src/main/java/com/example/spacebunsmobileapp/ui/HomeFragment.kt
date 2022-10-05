package com.example.spacebunsmobileapp.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy { findNavController() }

//    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.btnOrdernow.setOnClickListener {
            nav.popBackStack()
            nav.navigate(R.id.menuFragment)
        }
        binding.btnDelivery.setOnClickListener {
            nav.navigate(R.id.deliveryFragment)
        }
        binding.btnPickup.setOnClickListener {
            if (LocalTime.now().isBefore(LocalTime.parse("22:00:00")) && LocalTime.now().isAfter(LocalTime.parse("09:00:00"))) {
                nav.navigate(R.id.dateTimeFragment)
            }else{
                Snackbar.make(binding.root, "Sorry! We are closed!", Snackbar.LENGTH_SHORT).show()
            }
    }


        return binding.root
    }

//    override fun onResume() {
//        nav.popBackStack()
//        super.onResume()
//    }

}