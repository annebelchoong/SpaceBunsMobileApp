package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentAccountBinding
import com.example.spacebunsmobileapp.databinding.FragmentMenuBinding
import com.example.spacebunsmobileapp.util.ProductAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private val nav by lazy { findNavController() }
    private val vm:ProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        binding.fabCart.setOnClickListener{
            nav.popBackStack()
            nav.navigate(R.id.cartFragment)
        }

        val adapter = ProductAdapter() {holder, product ->
            holder.binding.root.setOnClickListener {
                nav.navigate(R.id.productDetailFragment, bundleOf("id" to product.productId))
            }
        }

        binding.rvProducts.adapter = adapter

        lifecycleScope.launch {
            val product = vm.getAll()
            adapter.submitList(product)
            binding.txtCount.text="${product.size} Record(s)"
        }

//        vm.getAllAll().observe(viewLifecycleOwner){
//            adapter.submitList(it)
//        }

        return binding.root
    }
}
