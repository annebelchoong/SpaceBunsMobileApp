package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.util.ProductAdapter
import com.example.spacebunsmobileapp.util.cropToBlob
import com.example.spacebunsmobileapp.util.setImageBlob
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.Cart
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.data.User
import com.example.spacebunsmobileapp.databinding.FragmentHomeBinding
import com.example.spacebunsmobileapp.databinding.FragmentProductDetailBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy {arguments?.getString("id","")?: ""}



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        binding.btnBack.setOnClickListener {
            nav.navigate(R.id.menuFragment)
        }

        lifecycleScope.launch {
            val product = vm.get(id)!!
            binding.imageView3.setImageBlob(product.photo)
            binding.txtProductName.text = product.name
            binding.txtDesc.text = product.desc
            vm.productId = product.id
            binding.txtTotalPrice.text = product.price.toString()

        }

        binding.btnAddToCart.setOnClickListener {
            addToCart()
            Snackbar.make(binding.root, "Product Added to Cart!", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }


    private fun addToCart() {
            val c = Cart()
            val u = User()

            c.productId = vm.productId
            c.productName = binding.txtProductName.text.toString()
            c.quantity = binding.txtQuantity.text.toString().toIntOrNull() ?: 1
            c.price = binding.txtTotalPrice.text.toString().toDoubleOrNull()?: 0.00
            c.totalPrice = c.quantity * c.price
            c.photo = binding.imageView3.cropToBlob(300, 300)
            u.customerId = "U001"

            vm.setCart(c,u)

            nav.navigateUp()

    }
}