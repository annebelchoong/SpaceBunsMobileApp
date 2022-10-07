package com.example.spacebunsmobileapp.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.CustomerViewModel
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentDeliveryBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class DeliveryFragment : DialogFragment() {
    private lateinit var binding: FragmentDeliveryBinding
    private val nav by lazy { findNavController() }
    private val vm: CustomerViewModel by activityViewModels()
    lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDeliveryBinding.inflate(inflater, container, false)

        binding.btnContinue.setOnClickListener {
            if (binding.edtNewAddress.text.toString() != "") {
                nav.navigate(R.id.dateTimeFragment)
//                lifecycleScope.launch{
//                    setAddress(binding.edtNewAddress.text.toString())
//                }
            } else {
                Snackbar.make(
                    binding.root,
                    "Please enter your address for delivery!",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

//        vm.address = binding.edtNewAddress.text.toString()

        return binding.root
    }
//    private suspend fun setAddress(add: String){
//        auth = FirebaseAuth.getInstance()
//        val user = auth.currentUser
//
//        if (user != null) {
//            vm.setAdd(add, user.uid)
//        }
//    }
}