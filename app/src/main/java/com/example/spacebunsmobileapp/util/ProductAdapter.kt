package com.example.spacebunsmobileapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebunsmobileapp.data.Product
import com.example.spacebunsmobileapp.databinding.ItemProductBinding

class ProductAdapter (
    val fn: (ViewHolder, Product) -> Unit ={ _, _ ->}
    ): ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallBack) {

        companion object DiffCallBack: DiffUtil.ItemCallback<Product>(){
            override fun areItemsTheSame(a: Product, b: Product) = a.productId == b.productId
            override fun areContentsTheSame(a: Product, b: Product) = a == b
        }
        class ViewHolder(val binding: ItemProductBinding): RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val product = getItem(position)

            holder.binding.txtName.text = product.name
            holder.binding.txtPrice.text = product.price.toString()
            holder.binding.imageView.setImageBlob(product.photo)

//        holder.binding.txtCount.text = "${product.count} Order(s)"

            fn(holder,product)
        }

}