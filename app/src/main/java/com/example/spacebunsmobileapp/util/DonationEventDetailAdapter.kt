package com.example.spacebunsmobileapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebunsmobileapp.data.DonationEventDetail
import com.example.spacebunsmobileapp.databinding.ItemDonationBinding

class DonationEventDetailAdapter(
    val dn: (ViewHolder, DonationEventDetail) -> Unit = { _, _ -> }
) : ListAdapter<DonationEventDetail, DonationEventDetailAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<DonationEventDetail>() {
        override fun areItemsTheSame(a: DonationEventDetail, b: DonationEventDetail) =
            a.donationId == b.donationId

        override fun areContentsTheSame(a: DonationEventDetail, b: DonationEventDetail) = a == b
    }

    class ViewHolder(val binding: ItemDonationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ItemDonationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val donationDetail = getItem(position)

        holder.binding.txtDonationId.text = donationDetail.donationId
        holder.binding.txtDonorName.text = donationDetail.donorName
        holder.binding.txtDonationAmount.text = (donationDetail.donationAmount).toString()
//        holder.binding.txtUsedCount.text = donation.usedCount.toString() + " times" // count

        dn(holder, donationDetail)
    }
}