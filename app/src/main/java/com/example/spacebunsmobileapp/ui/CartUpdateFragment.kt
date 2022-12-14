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
import com.example.spacebunsmobileapp.databinding.FragmentCartUpdateBinding
import com.example.spacebunsmobileapp.util.cropToBlob
import com.example.spacebunsmobileapp.util.setImageBlob
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class CartUpdateFragment : Fragment() {
    private lateinit var binding: FragmentCartUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()
    lateinit var auth: FirebaseAuth


    private val id by lazy {arguments?.getString("id","")?: ""}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartUpdateBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        lifecycleScope.launch {
            if(user != null){
                val cart = vm.getCart(id, user.uid)!!
                binding.imageView3.setImageBlob(cart.photo)
                binding.txtProductName.text = cart.productName
                binding.txtQuantity.setText(cart.quantity.toString())
                binding.txtTotalPrice.text = "${"%.2f".format(cart.price)}"
                vm.productId = cart.productId

            }
        }

        binding.btnAddToCart.setOnClickListener {
            addToCart()
            Snackbar.make(binding.root, "Product Updated!", Snackbar.LENGTH_SHORT).show()
        }

        return binding.root
    }


    private fun addToCart() {
        val c = Cart()
//        val u = User()
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        c.productId = vm.productId
        c.productName = binding.txtProductName.text.toString()
        c.quantity = binding.txtQuantity.text.toString().toIntOrNull() ?: 1
        c.price = binding.txtTotalPrice.text.toString().toDoubleOrNull()?: 0.00
        c.totalPrice = c.quantity * c.price
        c.photo = binding.imageView3.cropToBlob(300, 300)
//        u.customerId = "U001"

        if (user != null) {
            vm.setCart(c, user.uid)
        }

        nav.navigateUp()

    }
}