package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.FragmentHomeBinding
import com.example.spacebunsmobileapp.databinding.FragmentMiniDeliveryBinding

class MiniDeliveryFragment : Fragment() {
    private lateinit var binding: FragmentMiniDeliveryBinding
    private val nav by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMiniDeliveryBinding.inflate(inflater, container, false)

        binding.btnOrderNow.setOnClickListener { nav.navigate(R.id.menuFragment) }

        return binding.root
    }
}