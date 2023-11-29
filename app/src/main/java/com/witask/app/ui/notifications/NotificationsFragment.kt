package com.witask.app.ui.notifications

import android.R
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.witask.app.CoreActivity
import com.witask.app.MyApp
import com.witask.app.databinding.FragmentNotificationsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        var ph = MyApp.preferences.getString("photo","")
        if(!ph.isNullOrEmpty()) {
            binding.photo.setImageURI(Uri.parse(ph))
            binding.photo.setPadding(0)
        }
        var day = 1
        val days = mutableListOf<String>()
        while(day<=31) {
            days.add(day.toString())
            day++
        }
        binding.day2.adapter = ArrayAdapter(requireContext(),
            R.layout.simple_expandable_list_item_1,days)
        var month = 1
        val months = mutableListOf<String>()
        while(month<=12) {
            months.add(month.toString())
            month++
        }
        binding.month2.adapter = ArrayAdapter(requireContext(),
            R.layout.simple_expandable_list_item_1,months)
        var year = 1950
        val years = mutableListOf<String>()
        while(year<=2023) {
            years.add(year.toString())
            year++
        }
        binding.year2.adapter = ArrayAdapter(requireContext(),
            R.layout.simple_expandable_list_item_1,years)
        binding.textView16.setOnClickListener {
            MyApp.preferences.edit().remove("profile").remove("tasks").remove("categories").apply()
            requireActivity().finish()
        }
        binding.textView9.setOnClickListener {
            requireActivity().finish()
        }
        var profile = MyApp.preferences.getString("profile","")!!
        val arr = profile.split("|")
        binding.name.setText(arr[0])
        binding.surname.setText(arr[1])
        binding.country.setText(arr[2])
        var date = LocalDate.from(DateTimeFormatter.ofPattern("dd.MM.yyyy").parse(arr[4]))
        binding.year2.setSelection(date.year-1950)
        binding.day2.setSelection(date.dayOfMonth-1)
        binding.month2.setSelection(date.month.value-1)
        binding.name.addTextChangedListener {
            date = LocalDate.of(years[binding.year2.selectedItemPosition].toInt(),months[binding.month2.selectedItemPosition].toInt(),days[binding.day2.selectedItemPosition].toInt())
            MyApp.preferences.edit().putString("profile","${binding.name.text}|${binding.surname.text}|${binding.country.text}|${arr[3]}|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}").apply()
        }
        binding.surname.addTextChangedListener {
            date = LocalDate.of(years[binding.year2.selectedItemPosition].toInt(),months[binding.month2.selectedItemPosition].toInt(),days[binding.day2.selectedItemPosition].toInt())
            MyApp.preferences.edit().putString("profile","${binding.name.text}|${binding.surname.text}|${binding.country.text}|${arr[3]}|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}").apply()
        }
        binding.country.addTextChangedListener {
            date = LocalDate.of(years[binding.year2.selectedItemPosition].toInt(),months[binding.month2.selectedItemPosition].toInt(),days[binding.day2.selectedItemPosition].toInt())
            MyApp.preferences.edit().putString("profile","${binding.name.text}|${binding.surname.text}|${binding.country.text}|${arr[3]}|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}").apply()
        }
        binding.year2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                date = LocalDate.of(years[binding.year2.selectedItemPosition].toInt(),months[binding.month2.selectedItemPosition].toInt(),days[binding.day2.selectedItemPosition].toInt())
                MyApp.preferences.edit().putString("profile","${binding.name.text}|${binding.surname.text}|${binding.country.text}|${arr[3]}|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}").apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding.day2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                date = LocalDate.of(years[binding.year2.selectedItemPosition].toInt(),months[binding.month2.selectedItemPosition].toInt(),days[binding.day2.selectedItemPosition].toInt())
                MyApp.preferences.edit().putString("profile","${binding.name.text}|${binding.surname.text}|${binding.country.text}|${arr[3]}|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}").apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        binding.month2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                date = LocalDate.of(years[binding.year2.selectedItemPosition].toInt(),months[binding.month2.selectedItemPosition].toInt(),days[binding.day2.selectedItemPosition].toInt())
                MyApp.preferences.edit().putString("profile","${binding.name.text}|${binding.surname.text}|${binding.country.text}|${arr[3]}|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}").apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        (requireActivity() as CoreActivity).binding.floatingActionButton.visibility = View.INVISIBLE
        (requireActivity() as CoreActivity).binding.floatingActionButton.setOnClickListener(null)
        super.onResume()
    }
}