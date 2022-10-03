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
//        binding.btn.setOnClickListener {
//            binding.btn.setBackgroundTintList(ColorStateList.valueOf(R.color.black))
//
//        }

        return binding.root
    }

//    override fun onResume() {
//        nav.popBackStack()
//        super.onResume()
//    }

}