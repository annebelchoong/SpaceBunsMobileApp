package com.example.spacebunsmobileapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.data.DonationEventDetail
import com.example.spacebunsmobileapp.util.errorDialog
import com.example.spacebunsmobileapp.data.DonationEventDetailViewModel
import com.example.spacebunsmobileapp.databinding.FragmentMakeDonationBinding
import kotlinx.coroutines.launch

class MakeDonationFragment : Fragment() {
    private lateinit var binding: FragmentMakeDonationBinding
    private val nav by lazy { findNavController() }
    private val vm: DonationEventDetailViewModel by activityViewModels()

    private val id by lazy { arguments?.getString("donationEventId", "") ?: "" }

//    // TODO: Get content launcher
//    private val launcher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                binding.imgDonationEventPhoto.setImageURI(it.data?.data)  // result is path
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeDonationBinding.inflate(inflater, container, false)

        reset()
//        binding.imgDonationEventPhoto.setOnClickListener { select() }
        binding.btnReset.setOnClickListener { reset() }
        binding.btnDonate.setOnClickListener {
            lifecycleScope.launch {
                setDonation()
            }
        }

        return binding.root
    }

    private fun reset() {
//        binding.edtDonationId.text.clear()
//        binding.edtDonationEventName.text.clear()
        binding.edtDonationAmount.text.clear()
//        binding.imgDonationEventPhoto.setImageDrawable(null)
        binding.edtDonorName.requestFocus()
    }

//    private fun select() {
//        // TODO: Select file
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        launcher.launch(intent)
//    }

    private suspend fun setDonation() {
        val v = DonationEventDetail(
            donationEventId = id,
            donorName = binding.edtDonorName.text.toString().trim(),
//            donationEventName = binding.edtDonationEventName.text.toString().trim(),
            donationAmount = binding.edtDonationAmount.text.toString().toDoubleOrNull() ?: 0.00,
//            donationEventPhoto = binding.imgDonationEventPhoto.cropToBlob(300, 300)
        )

        val err = vm.validate(v)
        if (err != "") {
            errorDialog(err)
            return
        }

        // TODO: Set (insert)
//        vm.set(v)
        vm.setDonations(v)

        nav.navigateUp()
    }
}

