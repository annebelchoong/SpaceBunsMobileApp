package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DonationEventDetailViewModel : ViewModel() {
    private val donations =
        MutableLiveData<List<DonationEventDetail>>() // Search + filter + sort result

    private val col = Firebase.firestore.collection("donations")

    init {
        col.addSnapshotListener { value, _ -> donations.value = value?.toObjects() }
    }

    fun init() = Unit // dummy

//    fun get(id: String) = donations.value?.find { it.donationId == id }

//    // TODO(10): Return a specific category
//    suspend fun get(id: String): Donation? {
//        return DONATIONS
//            .document(id)
//            .get()
//            .await()
//            .toObject<Donation>()
//    }

    fun getAll() = donations // TODO

    fun delete(id: String) {
        col.document(id).delete()
    }

    fun deleteAll() {
        donations.value?.forEach { col.document(it.donationId).delete() }
    }

    fun set(d: DonationEventDetail) {
        col.document(d.donationId).set(d)
    }

    //----------------------------------------------------------------------------------------------

    private fun idExists(id: String) =
        donations.value?.any { it.donationId == id } ?: false
//    private fun codeExists(code: String) = donations.value?.any { it.voucherCode == code } ?: false

    fun validate(d: DonationEventDetail, insert: Boolean = true): String {
        val regexId = Regex("""^[A-Z][A-Z]\d{2}$""")
        var e = ""

        if (insert) {
//            e += if (d.donationId == "") "- Id is required.\n"
//            else if (!d.donationId.matches(regexId)) "- Id format is invalid (format: XX99).\n"
//            else if (idExists(d.donationId)) "- Id is duplicated.\n"
//            else ""
        }

//        e += if (d.donorName == "") "- Event Name is required.\n"
//        else if (d.donorName.length < 3) "- Event Name is too short (at least 3 letters).\n"
//        else ""
//
        e += if (d.donationAmount < 1) "- Donation Amount must be more than one.\n"
        else if (d.donationAmount == 0.00) "- Donation Amount cannot be zero.\n"
        else ""

        return e
    }

    suspend fun generateDonationId(): String {
        val donations = DONATIONS.get().await().toObjects<DonationEventDetail>()
        var id = ""
        if (donations == null) {
            for (d in donations) {
                d.donationId = "D1"
            }
        } else {
            val size = donations.size
            id = "D${size + 1}"
        }
        return id
    }

    suspend fun setDonations(e: DonationEventDetail) {
        val id = generateDonationId()
        DONATIONS.document(id).set(e)
    }

    fun getDonationAttributes(): List<String> {
//        return VOUCHERS.get().await().toObjects<Voucher>()
        return listOf("ID", "Name", "Goal", "Date")
    }
}