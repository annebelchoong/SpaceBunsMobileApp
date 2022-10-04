package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class CustomerViewModel : ViewModel() {
    //use live data for recycle view
    private val customers = MutableLiveData<List<Customer>>()

    // TODO: Initialization
    private val col = Firebase.firestore.collection("customers")

    //init code will not run until we call it
    init {
        col.addSnapshotListener { value, _ -> customers.value = value?.toObjects() } //_ means not using it, remove it
    }

    // ---------------------------------------------------------------------------------------------
    fun init() = Unit // void
    fun get(id: String) = customers.value?.find { it.id == id }
    fun getAll() = customers //live data

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        customers.value?.forEach { col.document(it.id).delete() }
    }

    fun set(f: Customer) {
        col.document(f.id).set(f)
    }

    //----------------------------------------------------------------------------------------------
    private fun idExists(id: String) = customers.value?.any { it.id == id } ?: false

    fun validate(f: Customer, insert: Boolean = true): String {
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

        e += if (f.phone == "") "- Phone no is required.\n"
        else if (f.phone.length < 11) "- Phone no is invalid.\n"
        else ""

        e += if (f.address == "") "- Address is required.\n"
        else if (f.address.length < 50) "- Address is invalid.\n"
        else ""

        e += if (f.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }
}