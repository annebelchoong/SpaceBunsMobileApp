package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
import java.time.format.DateTimeFormatter

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy {arguments?.getString("id","")?: ""}
    val custId = "U001"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val adapter = CartAdapter() {holder, cart ->
            holder.binding.btnEdit.setOnClickListener {
                nav.navigate(R.id.cartUpdateFragment, bundleOf("id" to cart.productId))
            }
            holder.binding.btnDelete.setOnClickListener {
                delete(cart.productId, custId)
            }
        }
        binding.rvCart.adapter = adapter
        binding.rvCart.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        lifecycleScope.launch {
            val product = vm.getCartLine(custId)
            adapter.submitList(product)
            binding.lblCount.text = "${product.size} Product(s)"

            val amount = vm.getAmount(custId)
            binding.txtAmount.text = "RM ${"%.2f".format(amount)}"
            binding.txtVoucher.text = 10.toString()
            var voucher = binding.txtVoucher.text.toString().toIntOrNull()?:0
            var subtotal = (amount * (100-voucher))/100
            binding.txtSubtotal.text = "RM ${"%.2f".format(subtotal)}"
            var grandTotal = subtotal + 3
            binding.txtGrandAmount.text = "RM ${"%.2f".format(grandTotal)}"
            binding.lblTotalPrice.text = "RM ${"%.2f".format(grandTotal)}"
        }

        binding.btnCheckout.setOnClickListener {
            if (LocalTime.now().isBefore(LocalTime.parse("22:00:00")) && LocalTime.now().isAfter(LocalTime.parse("09:00:00"))) {

            }else{
                Snackbar.make(binding.root, "Sorry! We are closed!", Snackbar.LENGTH_LONG).show()
            }
        }

        binding.btnApply.setOnClickListener {
            applyVoucher()
        }

        binding.lblDate.text = vm.dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy "))
        binding.lblTime.text = vm.dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))


        return binding.root
    }

    private fun applyVoucher() {

    }

    // for now user use string, need to change to user
    private fun delete(id:String, user: String){
        vm.delete(id, user)

        nav.navigateUp()
        nav.navigate(R.id.cartFragment)
    }

}