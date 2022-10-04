package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ProductViewModel: ViewModel() {

    var productId = ""
    val CART = Firebase.firestore.collection("usersTest")
    private val products = MutableLiveData<List<Product>>()
    private val cart = MutableLiveData<List<Cart>>()
    suspend fun get(id: String): Product? {
        return PRODUCTS // do not have count, only id and name
            .document(id)
            .get()
            .await()
            .toObject<Product>()
    }

    suspend fun getAll(): List<Product> {
        val product = PRODUCTS
            .get()
            .await()
            .toObjects<Product>()

//        for(os in product){
//            os.count = ORDERS
//                .whereEqualTo("productId", os.id)
//                .get()
//                .await()
//                .size()
//        }
        return product
    }

    fun getAllAll() = products

    // for cart
    fun setCart(c:Cart, u:User){
        CART.document(u.customerId).collection("cart").document(c.productId).set(c)
    }

    suspend fun getCust(id: String): User?{
        return CUST
            .document(id)
            .get()
            .await()
            .toObject<User>()
    }

}