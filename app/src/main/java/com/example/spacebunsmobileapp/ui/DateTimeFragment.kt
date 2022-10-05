package com.example.spacebunsmobileapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.util.rangeTo
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacebunsmobileapp.R
import com.example.spacebunsmobileapp.data.ProductViewModel
import com.example.spacebunsmobileapp.databinding.FragmentDateTimeBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class DateTimeFragment : DialogFragment() {
    private lateinit var binding: FragmentDateTimeBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()


    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy ")
    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDateTimeBinding.inflate(inflater, container, false)

        val arrayAdapterDate = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        arrayAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnDate.adapter = arrayAdapterDate

        val today = LocalDateTime.now()
        val tomorrow = today.plusDays(1).plusMinutes(15)
        val theDayAfter = today.plusDays(2).plusMinutes(30)
        arrayAdapterDate.add(today.format(dateFormatter))
        arrayAdapterDate.add(tomorrow.format(dateFormatter))
        arrayAdapterDate.add(theDayAfter.format(dateFormatter))

        val arrayAdapterTime = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        arrayAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnTime.adapter = arrayAdapterTime

        arrayAdapterTime.add(today.format(timeFormatter))
        arrayAdapterTime.add(tomorrow.format(timeFormatter))
        arrayAdapterTime.add(theDayAfter.format(timeFormatter))

        binding.spnDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit// return dummy function
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (binding.spnDate.selectedItem as String) {
                    today.format(dateFormatter) -> vm.dateTime = today
                    tomorrow.format(dateFormatter) -> vm.dateTime = tomorrow
                    theDayAfter.format(dateFormatter) -> vm.dateTime = theDayAfter
                }
            }
        }

        binding.spnTime.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit// return dummy function
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (binding.spnTime.selectedItem as String) {
                    today.format(timeFormatter) -> vm.dateTime = today
                    tomorrow.format(timeFormatter) -> vm.dateTime = tomorrow
                    theDayAfter.format(timeFormatter) -> vm.dateTime = theDayAfter
                }
            }
        }

        binding.btnOrderNowNow.setOnClickListener {
            nav.popBackStack()
            nav.popBackStack()
            nav.navigate(R.id.menuFragment)
        }

        return binding.root
    }


}