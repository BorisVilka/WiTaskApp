package com.witask.app.ui.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.witask.app.CoreActivity
import com.witask.app.MyApp
import com.witask.app.R
import com.witask.app.databinding.FragmentCalendarBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
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

    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalendarBinding.inflate(inflater,container,false)
        val tasks = MyApp.preferences.getStringSet("tasks",HashSet<String>())
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val now = LocalDate.now()
        var today = tasks!!.filter {
            val arr = it.split("|")
            if(arr[0]=="null") return@filter false
            val local = LocalDate.from(formatter.parse(arr[0]))
            return@filter now.isEqual(local)
        }.toMutableList()
        var ready = today.filter {
            return@filter it.split("|")[3]=="ready"
        }.toMutableList()
        val adapter = CalendarAdapter(ready)
        binding.calendarView2.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val date = LocalDate.of(i,i2+1,i3)
            today = tasks.filter {
                val arr = it.split("|")
                if(arr[0]=="null") return@filter false
                val local = LocalDate.from(formatter.parse(arr[0]))
                return@filter date.isEqual(local)
            }.toMutableList()
            adapter.setNewData(today)
        }
        binding.today.adapter = adapter
        return binding.root
    }

    override fun onResume() {
        (requireActivity() as CoreActivity).binding.floatingActionButton.visibility = View.VISIBLE
        (requireActivity() as CoreActivity).binding.floatingActionButton.setImageResource(R.drawable.graph)
        (requireActivity() as CoreActivity).binding.floatingActionButton.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_core)
            navController.navigate(R.id.todayFragment)
        }
        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}