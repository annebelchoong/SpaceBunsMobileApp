package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentCartBinding
import com.example.spacebunsmobileapp.databinding.FragmentCheckoutTestBinding

class CheckoutTestFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutTestBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutTestBinding.inflate(inflater, container, false)


        return binding.root
    }
}