package com.witask.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.witask.app.databinding.FragmentRegistrationBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistrationFragment : Fragment() {
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

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        binding.name.addTextChangedListener {
            binding.name.error = null
        }
        binding.surname.addTextChangedListener {
            binding.surname.error = null
        }
        binding.country.addTextChangedListener {
            binding.country.error = null
        }
        binding.pass.addTextChangedListener {
            binding.pass.error = null
        }
        binding.confirm.addTextChangedListener {
            binding.confirm.error = null
        }
        var ph = ""
        binding.button.setOnClickListener {
            if(binding.name.text.isNullOrEmpty()) binding.name.error = "Name is empty"
            else if(binding.surname.text.isNullOrEmpty()) binding.surname.error = "Surname is empty"
            else if(binding.country.text.isNullOrEmpty()) binding.country.error = "Country is empty"
            else if(binding.pass.text.isNullOrEmpty()) binding.pass.error = "Password is empty"
            else if(binding.confirm.text.isNullOrEmpty() || binding.confirm.text.toString()!=binding.pass.text.toString()) binding.confirm.error = "Password is wrong"
            else {
                val profile = "${binding.name.text}|${binding.surname.text}|${binding.country.text}|${binding.pass.text}"
                val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                navController.navigate(R.id.action_registrationFragment_to_dateFragment,Bundle().apply { putString("profile",profile)
                    putString("photo",ph)
                })
            }
        }
        binding.photo.setOnClickListener {
            MainActivity.callback = {
                Log.d("TAG",it)
                ph = it
                binding.photo.setPadding(0)
                binding.photo.setImageURI(Uri.parse(it))
            }
            val intent = Intent().also {
                it.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
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
         * @return A new instance of fragment RegistrationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}