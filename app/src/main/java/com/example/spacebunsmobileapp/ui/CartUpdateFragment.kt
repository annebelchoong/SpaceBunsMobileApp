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
import com.example.spacebunsmobileapp.data.Cart
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.data.User
import com.example.spacebunsmobileapp.databinding.FragmentCartUpdateBinding
import com.example.spacebunsmobileapp.databinding.FragmentProductDetailBinding
import com.example.spacebunsmobileapp.util.cropToBlob
import com.example.spacebunsmobileapp.util.setImageBlob
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class CartUpdateFragment : Fragment() {
    private lateinit var binding: FragmentCartUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy {arguments?.getString("id","")?: ""}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartUpdateBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            val cart = vm.getCart(id, "U001")!!
            binding.imageView4.setImageBlob(cart.photo)
            binding.txtProductName2.text = cart.productName
            binding.txtQuantity2.setText(cart.quantity.toString())
            binding.txtTotalPrice2.text = cart.price.toString()
            vm.productId = cart.productId
        }

        binding.btnAddToCart2.setOnClickListener {
            addToCart()
            Snackbar.make(binding.root, "Product Updated!", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }


    private fun addToCart() {
        val c = Cart()
        val u = User()

        c.productId = vm.productId
        c.productName = binding.txtProductName2.text.toString()
        c.quantity = binding.txtQuantity2.text.toString().toIntOrNull() ?: 1
        c.price = binding.txtTotalPrice2.text.toString().toDoubleOrNull()?: 0.00
        c.totalPrice = c.quantity * c.price
        c.photo = binding.imageView4.cropToBlob(300, 300)
        u.customerId = "U001"

        vm.setCart(c,u)

        nav.navigateUp()

    }
}