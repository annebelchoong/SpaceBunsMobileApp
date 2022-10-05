package com.example.spacebunsmobileapp.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.data.Customer
import com.example.spacebunsmobileapp.data.CustomerViewModel
import com.example.spacebunsmobileapp.databinding.FragmentUpdateProfileBinding
import com.example.spacebunsmobileapp.util.cropToBlob
import com.example.spacebunsmobileapp.util.errorDialog
import com.example.spacebunsmobileapp.util.setImageBlob
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileFragment : Fragment() {
    private lateinit var binding: FragmentUpdateProfileBinding
    private val nav by lazy { findNavController() }
    private val vm: CustomerViewModel by activityViewModels()

    private val id by lazy { arguments?.getString("id") ?: "" }
    private val formatter = SimpleDateFormat("dd MMMM yyyy '-' hh:mm:ss a", Locale.getDefault())

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgUser.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)

        reset()
        binding.imgUser.setOnClickListener  { select() }
        binding.btnReset.setOnClickListener  { reset()  }
        binding.btnSave.setOnClickListener { submit() }

        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        val c = vm.get(id)

        if (c == null) {
            nav.navigateUp()
            return
        }

        binding.edtName.setText(c.name)
        binding.edtPhone.setText(c.phone.toString())
        binding.edtAddress.setText(c.address.toString())

        // TODO: Load photo and date
        binding.imgUser.setImageBlob(c.photo)
        binding.edtName.requestFocus()
    }

    private fun submit() {
        val c = Customer(
            name = binding.edtName.text.toString().trim(),
            phone = binding.edtPhone.text.toString(),
            address = binding.edtAddress.text.toString().trim(),
            // TODO: Photo
            photo = binding.imgUser.cropToBlob(300,300)
        )

        val err = vm.validate(c, false)
        if (err != "") {
            errorDialog(err)
            return
        }

        vm.set(c)
        nav.navigateUp()
    }

    private fun delete() {
        vm.delete(id)
        nav.navigateUp()
    }
}