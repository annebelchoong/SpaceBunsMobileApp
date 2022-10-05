package com.example.spacebunsmobileapp.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SpaceBunsViewModel: ViewModel() {

    private var fmenu = listOf<Menu>()
    private val product = MutableLiveData<List<Menu>>()

    private var cat = ""  //Search

    // Initialization
    private val col = Firebase.firestore.collection("products")  // ref

    init {  // code that will be executed in constructor
        viewModelScope.launch {
            MENU.addSnapshotListener { snap, _ ->
                if(snap == null) return@addSnapshotListener

                fmenu = snap.toObjects<Menu>()

                updateResult()
            }
        }
        col.addSnapshotListener { value, _ -> product.value = value?.toObjects() }
    }

    // ---------------------------------------------------------------------------------------------

    private fun updateResult(){
        var list = fmenu

        list = list.filter { m ->
            m.cat.contains(cat)
        }

       product.value = list
    }

    // dummy function to allow the ViewModel to execute earlier
    fun init() = Unit // Void

    fun getResult() = product

    fun search(cat: String){
        this.cat = cat
        updateResult()
    }


    fun getAll() = product // TODO


    fun set(f: Menu) {
        col.document(f.id).set(f)
    }






}
