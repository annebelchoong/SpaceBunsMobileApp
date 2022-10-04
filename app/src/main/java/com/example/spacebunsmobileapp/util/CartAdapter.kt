package com.example.spacebunsmobileapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebunsadminapp.util.setImageBlob
import com.example.spacebunsmobileapp.data.Cart
import com.example.spacebunsmobileapp.databinding.ItemProductBinding
import com.example.spacebunsmobileapp.databinding.ItemProductLineBinding

class CartAdapter (
    val fn: (ViewHolder, Cart) -> Unit ={ _, _ ->}
    ): ListAdapter<Cart, CartAdapter.ViewHolder>(DiffCallBack) {

        companion object DiffCallBack: DiffUtil.ItemCallback<Cart>(){
            override fun areItemsTheSame(a: Cart, b: Cart) = a.productId == b.productId
            override fun areContentsTheSame(a: Cart, b: Cart) = a == b
        }
        class ViewHolder(val binding: ItemProductLineBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemProductLineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val productLine = getItem(position)

            holder.binding.lblProductName.text = productLine.productName
            holder.binding.lblQuantity.text = "x ${productLine.quantity}"
            var totalPrice = productLine.quantity * productLine.price
            holder.binding.lblPrice.text = " RM ${totalPrice}"
            holder.binding.imageView2.setImageBlob(productLine.photo)

//        holder.binding.txtCount.text = "${productLine.count} Order(s)"

            fn(holder,productLine)
        }
}