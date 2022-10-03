package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentHomeBinding
import com.example.spacebunsmobileapp.databinding.FragmentProductDetailBinding
import com.example.spacebunsmobileapp.util.setImageBlob
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy {arguments?.getString("id","")?: ""}


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            val product = vm.get(id)!!
            binding.imageView3.setImageBlob(product.photo)
            binding.txtProductName.text = product.name
            binding.txtDesc.text = product.desc
        }

        binding.btnAddToCart.setOnClickListener {
            nav.navigateUp()

        }

        return binding.root
    }
}