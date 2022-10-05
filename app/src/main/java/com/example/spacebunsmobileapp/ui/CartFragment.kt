package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()
    lateinit var auth: FirebaseAuth

    private val id by lazy {arguments?.getString("id","")?: ""}
//    val custId = "U001"
    val df = DecimalFormat("#.##")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val adapter = CartAdapter() {holder, cart ->
            holder.binding.btnEdit.setOnClickListener {
                nav.navigate(R.id.cartUpdateFragment, bundleOf("id" to cart.productId))
            }
            holder.binding.btnDelete.setOnClickListener {
                if (user != null) {
                    delete(cart.productId, user.uid)
                }
            }
        }
        binding.rvCart.adapter = adapter
        binding.rvCart.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        lifecycleScope.launch {
            if(user != null) {
                val product = vm.getCartLine(user.uid)
                adapter.submitList(product)
                binding.lblCount.text = "${product.size} Product(s)"

                val amount = vm.getAmount(user.uid)
                binding.txtAmount.text = "RM ${"%.2f".format(amount)}"
                var voucher = binding.txtVoucher.text.toString().toDoubleOrNull() ?: 0.00
                var subtotal = amount - voucher
                binding.txtSubtotal.text = "RM ${"%.2f".format(subtotal)}"
                vm.grandTotal = subtotal + 3
                binding.txtGrandAmount.text = "RM ${"%.2f".format(vm.grandTotal)}"
                binding.lblTotalPrice.text = "RM ${"%.2f".format(vm.grandTotal)}"
            }

        }

        binding.btnCheckout.setOnClickListener {
            if (LocalTime.now().isBefore(LocalTime.parse("22:00:00")) && LocalTime.now().isAfter(LocalTime.parse("09:00:00"))) {

            }else{
                Snackbar.make(binding.root, "Sorry! We are closed!", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnApply.setOnClickListener {
//            applyVoucher(binding.edtVoucher.text.toString())
            lifecycleScope.launch {
                applyVoucher(binding.edtVoucher.text.toString().uppercase().trim())

            }
        }

        binding.lblDate.text = vm.dateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy "))
        binding.lblTime.text = vm.dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))


        return binding.root
    }

    private suspend fun applyVoucher(code: String) {
        val context = requireActivity().application
        val voucher = vm.getVoucher()!!
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (code.isEmpty()) {
            Toast.makeText(context, "No voucher!", Toast.LENGTH_SHORT).show()
        }
            for (v in voucher) {
                if (code == v.voucherCode && user != null) {
                    var total = vm.getAmount(user.uid)
                    var discount = v.discountPercentage / 100
                    var deduct = total * discount
//                binding.txtVoucher.text = (total * (v.discountPercentage/100 )).toString()
                    binding.txtVoucher.text = df.format(deduct).toString()
                    var subtotal = total - deduct
                    binding.txtSubtotal.text = "RM ${"%.2f".format(subtotal)}"
                    vm.grandTotal = subtotal + 3
                    binding.txtGrandAmount.text = "RM ${"%.2f".format(vm.grandTotal)}"
                    binding.lblTotalPrice.text = "RM ${"%.2f".format(vm.grandTotal)}"
                }
                else{
                    Toast.makeText(context, "Voucher not available!", Toast.LENGTH_SHORT).show()

                }
            }
    }



    // for now user use string, need to change to user
    private fun delete(id:String, userId: String){
        vm.delete(id, userId)

        nav.navigateUp()
        nav.navigate(R.id.cartFragment)
    }



}
