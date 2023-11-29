package com.witask.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.navigation.Navigation
import com.witask.app.databinding.FragmentDateBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentDateBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDateBinding.inflate(inflater,container,false)
        var day = 1
        val days = mutableListOf<String>()
        while(day<=31) {
            days.add(day.toString())
            day++
        }
        binding.day.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,days)
        var month = 1
        val months = mutableListOf<String>()
        while(month<=12) {
            months.add(month.toString())
            month++
        }
        binding.month.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,months)
        var year = 1950
        val years = mutableListOf<String>()
        while(year<=2023) {
            years.add(year.toString())
            year++
        }
        binding.year.adapter = ArrayAdapter(requireContext(),android.R.layout.simple_expandable_list_item_1,years)
        var profile = requireArguments().getString("profile")
        val arr = profile!!.trim().split("|")
        binding.textView.text = "Hi ${arr[0]} ${arr[1]}"
        var ph = requireArguments().getString("photo")
        if(ph!="") {
            binding.photo.setPadding(0)
            binding.photo.setImageURI(Uri.parse(ph))
        }
        binding.photo.setOnClickListener {
            MainActivity.callback = {
                Log.d("TAG",it)
                ph = it
                binding.photo.setPadding(0)
                binding.photo.setImageURI(Uri.parse(it))
            }
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }
        binding.button.setOnClickListener {
            val date = LocalDate.of(years[binding.year.selectedItemPosition].toInt(),months[binding.month.selectedItemPosition].toInt(),days[binding.day.selectedItemPosition].toInt())
            profile = "$profile|${DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date)}"
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.navigate(R.id.action_dateFragment_to_selectCategoriesFragment,Bundle().apply { putString("profile",profile)
                putString("photo",ph)
            })
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}