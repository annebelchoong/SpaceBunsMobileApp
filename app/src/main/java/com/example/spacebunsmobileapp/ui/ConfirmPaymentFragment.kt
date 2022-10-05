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
import com.example.spacebunsmobileapp.data.OrderDetails
import com.example.spacebunsmobileapp.data.Orders
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentConfirmPaymentBinding
import com.example.spacebunsmobileapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.time.LocalTime

class ConfirmPaymentFragment : Fragment() {
    private lateinit var binding: FragmentConfirmPaymentBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val id by lazy {arguments?.getString("id","")?: ""}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConfirmPaymentBinding.inflate(inflater, container, false)

        binding.btnConfirmPayment.setOnClickListener {
            lifecycleScope.launch{
                setOrder(id)

            }
        }
        return binding.root
    }

    private suspend fun setOrder(id: String){
        val user = vm.getUserId(id)
        val order = Orders()

        if (user != null) {
            order.address = user.address
            order.totalPrice = vm.grandTotal
            order.voucherId = vm.voucher
            order.orderStatusId = "P"
            order.subtotal = vm.subtotal
            order.customerId = id
            order.paymentMethod = vm.paymentMethod
        }

        vm.setOrders(order)
        setOrderDetails(id, order.orderId)

    }
    private suspend fun setOrderDetails(uId: String, orderId: String){
        val cartline = vm.getCartLine(uId)
        val orderD = OrderDetails()

        orderD.orderId = id
        for (c in cartline){
            orderD.productId = c.productId
            orderD.quantity = c.quantity
            orderD.price = c.price
            orderD.productName = c.productName
            vm.setOrderDetail(orderD)
        }


    }
}

