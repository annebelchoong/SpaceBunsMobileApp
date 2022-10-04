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
    var dateTime = LocalDateTime.now()
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

    suspend fun getCart(id: String, u: String): Cart? {
        return CART // do not have count, only id and name
            .document(u)
            .collection("cart")
            .document(id)
            .get()
            .await()
            .toObject<Cart>()
    }

    suspend fun getAll(): List<Product> {
        val product = PRODUCTS
            .get()
            .await()
            .toObjects<Product>()

        return product
    }

    fun getAllAll() = products

    // for cart
    fun setCart(c:Cart, u:User){
        CART.document(u.customerId).collection("cart").document(c.productId).set(c)
    }

    suspend fun getUserId(id: String): User? {
        return CUST // do not have count, only id and name
            .document(id)
            .get()
            .await()
            .toObject<User>()
    }


    // put u as string first but neet to change to user
    suspend fun getCartLine(u:String): List<Cart> {
        val user = getUserId(u)!!

        val getProducts = CART
            .document(user.customerId)
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

}