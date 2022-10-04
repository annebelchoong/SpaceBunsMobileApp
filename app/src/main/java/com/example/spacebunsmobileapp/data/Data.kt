package com.example.spacebunsmobileapp.data

import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

data class Order(
    var id: String=""
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
