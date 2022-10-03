package com.example.spacebunsadminapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebunsmobileapp.data.Menu
import com.example.spacebunsmobileapp.databinding.FragmentMenuItemBinding


class ProductAdapter (
    val fn: (ViewHolder, Menu) -> Unit = { _, _ -> }
) : ListAdapter<Menu, ProductAdapter.ViewHolder>(DiffCallback){

    companion object DiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(a: Menu, b: Menu)    = a.id == b.id
        override fun areContentsTheSame(a: Menu, b: Menu) = a == b
    }

    class ViewHolder(val binding: FragmentMenuItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = getItem(position)

        //holder.binding.txtId.text   = product.id
        //holder.binding.txtCat.text = ("--> " + product.cat + " <--")
        holder.binding.txtName.text = product.name
        holder.binding.txtDesc.text  = product.desc

        // TODO: Load photo blob (use extension method)
        // holder.binding.imgPhoto.setImageDrawable(null) // default doesn't work
        holder.binding.imgPhoto.setImageBlob(product.photo)

        fn(holder, product)
    }

}


