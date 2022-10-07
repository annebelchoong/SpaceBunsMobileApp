package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime

class ProductViewModel: ViewModel() {

    var productId = ""
    var grandTotal = 0.00
    var subtotal = 0.00
    var address = ""
    var orderType= "" // for delivery or pick up
    var voucher = ""
    var dateTime = LocalDateTime.now()
    var paymentMethod = ""
    val CART = Firebase.firestore.collection("customers")
    private val products = MutableLiveData<List<Menu>>()
    private val cart = MutableLiveData<List<Cart>>()

    suspend fun get(id: String): Menu? {
        return PRODUCTS // do not have count, only id and name
            .document(id)
            .get()
            .await()
            .toObject<Menu>()
    }
    

    suspend fun getCart(id: String, u: String): Cart? {
        return CART // do not have count, only id and name
            .document(u)
            .collection("cart")
            .document(id)
            .get()
            .await()
            .toObject<Cart>()
    }

    suspend fun getAll(): List<Menu> {
        val product = MENU
            .get()
            .await()
            .toObjects<Menu>()

        return product
    }

    fun getAllAll() = products

    // for cart
//    fun setCart(c:Cart, u:User){
        fun setCart(c:Cart, u:String){
        CART.document(u).collection("cart").document(c.productId).set(c)
    }

    suspend fun getUserId(id: String): Customer? {
        return CUST // do not have count, only id and name
            .document(id)
            .get()
            .await()
            .toObject<Customer>()
    }


    // put u as string first but neet to change to user
    suspend fun getCartLine(u:String): List<Cart> {
        val user = getUserId(u)!!

        val getProducts = CART
            .document(user.id)
            .collection("cart")
            .get()
            .await()
            .toObjects<Cart>()


        return getProducts
    }

    //u need to change to user
    fun delete(id: String, u: String) {
        CART.document(u).collection("cart").document(id).delete()

    }

    suspend fun getAmount(u:String): Double {
        val user = getUserId(u)!!

        val getProducts = CART
            .document(user.id)
            .collection("cart")
            .get()
            .await()
            .toObjects<Cart>()

        var total = 0.00
        var final = 0.00
        for(p in getProducts){
            total = p.price * p.quantity
            final += total
        }

        return final
    }

    suspend fun getVoucher(): List<Voucher>{
        val vouchers = VOUCHERS
            .get()
            .await()
            .toObjects<Voucher>()

        return vouchers
    }

    fun set(o:Orders){
        ORDERS.document(o.orderId).set(o)
    }

    suspend fun generateOrderId(): String{
        val order = ORDERS.get().await().toObjects<Orders>()
        var id = ""
        if (order == null){
            for(o in order){
                o.orderId = "S1"
            }
        } else {
            val size = order.size
            id = "S${size+1}"
        }

        return id
    }

    suspend fun setOrders(o: Orders){
        val id = generateOrderId()
        ORDERS.document(id).set(o)
    }

    suspend fun generateOrderDetailId(): String{
        val orderDetail = ORDERDETAIL.get().await().toObjects<OrderDetails>()
        var id = ""
        if (orderDetail == null){
            for(o in orderDetail){
                o.id = "OD1"
            }
        } else {
            val size = orderDetail.size
            id = "OD${size+1}"
        }

        return id
    }

    suspend fun setOrderDetail(od: OrderDetails){
        val id = generateOrderDetailId()
        ORDERDETAIL.document(id).set(od)
    }

}