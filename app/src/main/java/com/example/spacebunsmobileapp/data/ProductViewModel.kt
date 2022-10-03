package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class ProductViewModel: ViewModel() {

    val products = MutableLiveData<List<Product>>()
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
}