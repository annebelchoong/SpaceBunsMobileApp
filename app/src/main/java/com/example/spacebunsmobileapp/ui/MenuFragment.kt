package com.example.spacebunsmobileapp.ui

import android.content.ContentValues.TAG
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.spacebunsmobileapp.util.ProductAdapter
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.MENU
import com.example.spacebunsmobileapp.data.SpaceBunsViewModel
import com.example.spacebunsmobileapp.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private val nav by lazy { findNavController() }
    private val vm: SpaceBunsViewModel by activityViewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        adapter = ProductAdapter { holder, product ->
            holder.binding.root.setOnClickListener {
                nav.navigate(R.id.productDetailFragment, bundleOf("id" to product.id))
            }
        }
        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))



        vm.getResult().observe(viewLifecycleOwner){ menu ->
            adapter.submitList(menu)
        }

        binding.btnAll.setOnClickListener { vm.search("") }
        binding.ivChicken.setOnClickListener { vm.search("Chicken") }
        binding.ivBeef.setOnClickListener { vm.search("Beef") }
        binding.ivLamb.setOnClickListener { vm.search("Lamb") }
        binding.ivFish.setOnClickListener { vm.search("Fish") }
        /*vm.getAll().observe(viewLifecycleOwner){
            adapter.submitList(it)
        }*/


        /*// TODO: Get all
        vm.getAll().observe(viewLifecycleOwner) {
           adapter.submitList(it)
        }*/

        return binding.root
    }

}
