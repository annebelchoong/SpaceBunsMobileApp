package com.example.spacebunsmobileapp.data

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.util.*

data class Order(
    var id: String=""
)

data class Customer(
    @DocumentId
    var id: String = "",
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var phone: String = "",
    var address: String = "",
    var photo: Blob   = Blob.fromBytes(ByteArray(0)),
)

data class Menu(
    @DocumentId
    var id: String = "",
    var cat: String = "",
    var name: String = "",
    var desc: String = "",
    var price: Double = 0.00,
    var photo: Blob = Blob.fromBytes(ByteArray(0)),  // empty bytes
    var date: Date = Date() // current Date
)

var MENU = FirebaseFirestore.getInstance().collection("products")

data class Cart(
    @DocumentId
    var productId: String = "",
    var productName: String = "",
    var quantity: Int = 0,
    var price: Double = 0.00,
    var photo: Blob = Blob.fromBytes(ByteArray(0)),
){
    @get:Exclude
    var totalPrice: Double = 0.0
//    var product: Cart = Cart()
}

data class Voucher(
    @DocumentId
    var voucherId: String = "",
    var voucherCode: String = "",
    var discountPercentage: Double = 0.0,
    var usedCount: Int = 0,
)
val PRODUCTS = Firebase.firestore.collection("products")
//val CART = Firebase.firestore.collection("usersTest").document("U001").collection("cart")
val CUST = Firebase.firestore.collection("customers")
val VOUCHERS = Firebase.firestore.collection("vouchers")

