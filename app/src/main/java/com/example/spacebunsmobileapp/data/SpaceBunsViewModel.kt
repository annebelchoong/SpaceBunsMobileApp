package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class SpaceBunsViewModel: ViewModel() {

    private val product = MutableLiveData<List<Menu>>()

    // Initialization
    private val col = Firebase.firestore.collection("products")  // ref

    init {  // code that will be executed in constructor
        col.addSnapshotListener { value, _ -> product.value = value?.toObjects() }
    }

    // ---------------------------------------------------------------------------------------------

    // dummy function to allow the ViewModel to execute earlier
    fun init() = Unit // Void

    fun get(id: String) = product.value?.find { it.id == id }

    fun getAll() = product // TODO

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun set(f: Menu) {
        col.document(f.id).set(f)
    }

    private fun idExists(id: String) = product.value?.any { it.id == id } ?: false

    fun validate(f: Menu, insert: Boolean = true): String {
        val regexId = Regex("""^[A-Z]\d{3}$""")
        var e = ""

        if (insert) {
            e += if (f.id == "") "- Id is required.\n"
            else if (!f.id.matches(regexId)) "- Id format is invalid (format: X999).\n"
            else if (idExists(f.id)) "- Id is duplicated.\n"
            else ""
        }

        e += if (f.name == "") "- Name is required.\n"
        else if (f.name.length < 3) "- Name is too short (at least 3 letters).\n"
        else ""

        e += if (f.desc == "") "- Description is required.\n"
        else if (f.desc.length < 10) "- Description is too short (at least 10 letters).\n"
        else ""

        // TODO: Validate if photo is empty
        e += if (f.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }



}
