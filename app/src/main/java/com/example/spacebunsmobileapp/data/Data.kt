package com.example.spacebunsmobileapp.data

import android.content.Context
import android.graphics.BitmapFactory
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.util.toBlob
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


data class Customer (
    @DocumentId
    var id      : String = "",
    var email   : String = "",
    var password: String = "",
    var name    : String = "",
    var phone   : String = "",
    var address : String = "",
    var photo   : Blob   = Blob.fromBytes(ByteArray(0)),
)

//val CUSTOMER = Firebase.firestore.collection("customers")

//fun RESTORE_USERS(ctx: Context) {
//    // (1) DELETE users
//    USERS.get().addOnSuccessListener { snap ->
//        for (doc in snap.documents) {
//            USERS.document(doc.id).delete()
//        }
//    }
//    // (2) ADD users
//    val user1 = User(
//        email    = "1@gmail.com",
//        password = "password",
//        name     = "Bae Suzy",
//        photo    = BitmapFactory.decodeResource(ctx.resources, R.drawable.suzy).toBlob(),
//    )
//    USERS.document().set(user1)
//
//    val user2 = User(
//        email    = "2@gmail.com",
//        password = "password",
//        name     = "Lee Jieun",
//        photo    = BitmapFactory.decodeResource(ctx.resources, R.drawable.jieun).toBlob(),
//    )
//    USERS.document().set(user2)
//
//}

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

data class User(
    @DocumentId
    var customerId: String = ""
)

data class DonationEvent(
    @DocumentId
    var donationEventId: String = "",
    var donationEventName: String = "",
    var donationGoal: Double = 0.00,
    var donationProgress: Double = 0.00,
    var donationEventPhoto: Blob = Blob.fromBytes(ByteArray(0)),
    var donationStartDate: Date = Date() // current Date
) {
    // TODO(1): Additional field: [count] and [toString]
    @get:Exclude    // excluded from firestore
    var count: Int = 0
    override fun toString() = donationEventName     // for spinner
}

data class DonationEventDetail(
    @DocumentId
    var donationId: String = "",
    var donorName: String = "",
    var donationAmount: Double = 0.00,
    var donationEventId: String = "",
    var donationDate: Date = Date() // current Date
) {
    // TODO(2): Additional field: [dEvent]

    @get: Exclude
    var donationEvent: DonationEvent = DonationEvent()
}

val DONATIONS = Firebase.firestore.collection("donations")

val DONATION_EVENTS = Firebase.firestore.collection("donationEvents")

val PRODUCTS = Firebase.firestore.collection("products")
//val CART = Firebase.firestore.collection("usersTest").document("U001").collection("cart")
val CUST = Firebase.firestore.collection("usersTest")

