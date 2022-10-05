package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.databinding.FragmentRewardBinding
import com.stripe.android.PaymentConfiguration

class RewardFragment : Fragment() {
    private lateinit var binding: FragmentRewardBinding
    private val nav by lazy { findNavController() }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRewardBinding.inflate(inflater, container, false)
        binding.btnCheckoutTest.setOnClickListener { nav.navigate(R.id.checkoutFragment) }

//        PaymentConfiguration.init(applicationContext,resources.getString(R.string.stripe_publishable_key))

        return binding.root
    }
}