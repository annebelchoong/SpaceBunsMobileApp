package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.DonationEventViewModel
import com.example.spacebunsmobileapp.databinding.FragmentDeliveryBinding
import com.example.spacebunsmobileapp.databinding.FragmentDonationBinding
import com.example.spacebunsmobileapp.databinding.FragmentHomeBinding
import com.example.spacebunsmobileapp.util.DonationEventAdapter
import kotlinx.coroutines.launch

class DonationFragment : Fragment() {
    private lateinit var binding: FragmentDonationBinding
    private val nav by lazy { findNavController() }
    private val vm: DonationEventViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonationBinding.inflate(inflater, container, false)
//        binding.btnCheckoutTest.setOnClickListener { nav.navigate(R.id.donationEventDetailFragment) }

        val adapter = DonationEventAdapter() { holder, donationEvent ->
            holder.binding.root.setOnClickListener {
                nav.navigate(
                    R.id.donationEventDetailFragment,
                    bundleOf("donationEventId" to donationEvent.donationEventId)
                )
            }
        }

        binding.rvDonations.adapter = adapter
        binding.rvDonations.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        // TODO(8): Load categories data into recycler view -> launch block
        lifecycleScope.launch {
            val donationEvents = vm.getAll()
            adapter.submitList(donationEvents)
            binding.txtDonationCount.text = "${donationEvents.size} Records(s)"
        }

        return binding.root
    }
}