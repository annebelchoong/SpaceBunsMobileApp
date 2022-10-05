package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.FragmentDonationBinding

class DonationFragment : Fragment() {
    private lateinit var binding: FragmentDonationBinding
    private val nav by lazy { findNavController() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationBinding.inflate(inflater, container, false)
        binding.btnCheckoutTest.setOnClickListener { nav.navigate(R.id.checkoutFragment) }

//        PaymentConfiguration.init(applicationContext,resources.getString(R.string.stripe_publishable_key))

        return binding.root
    }
}