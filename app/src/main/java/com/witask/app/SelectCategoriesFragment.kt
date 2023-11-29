package com.witask.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.witask.app.databinding.FragmentSelectCategoriesBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectCategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectCategoriesFragment : Fragment() {
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

    private lateinit var binding: FragmentSelectCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectCategoriesBinding.inflate(inflater,container,false)
        val checkboxs = arrayOf(binding.checkBox,binding.checkBox2,binding.checkBox3,binding.checkBox4,binding.checkBox5,binding.checkBox6,binding.checkBox7,binding.checkBox9)
        binding.button.setOnClickListener {
            val set = HashSet<String>()
            for(i in checkboxs) if(i.isChecked) set.add(i.text.toString())
            MyApp.preferences.edit().putString("profile",requireArguments().getString("profile")).putStringSet("categories",set).putString("photo",requireArguments().getString("photo")).apply()
            startActivity(Intent(requireContext(),CoreActivity::class.java))
            requireActivity().finish()
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
         * @return A new instance of fragment SelectCategoriesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}