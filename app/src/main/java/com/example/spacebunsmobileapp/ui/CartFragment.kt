package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentCartBinding
import com.example.spacebunsmobileapp.util.CartAdapter
import com.example.spacebunsmobileapp.util.ProductAdapter
import com.example.spacebunsmobileapp.util.setImageBlob
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalTime

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy {arguments?.getString("id","")?: ""}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val adapter = CartAdapter()
        binding.rvCart.adapter = adapter
        binding.rvCart.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        lifecycleScope.launch {
            val product = vm.getCartLine("U001")
            adapter.submitList(product)
            binding.lblCount.text = "${product.size} Product(s)"
        }

        binding.btnCheckout.setOnClickListener {
            if (LocalTime.now().isAfter(LocalTime.parse("22:00:00")) && LocalTime.now().isBefore(
                    LocalTime.parse("09:00:00"))) {
//                nav.navigate(R.id.dateTimeFragment)
            }else{
                Snackbar.make(binding.root, "Sorry! We are closed!", Snackbar.LENGTH_LONG).show()
            }
        }


        return binding.root
    }
}