package com.witask.app.ui.calendar

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.witask.app.CoreActivity
import com.witask.app.MyApp
import com.witask.app.R
import com.witask.app.databinding.FragmentTodayBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodayFragment : Fragment() {
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
    val months = arrayOf(
        mutableListOf(),
        mutableListOf("Su","Mo","Tu","Wed","Th","Fr","Sut"),
        mutableListOf("January","February","March","April","May","June","July","August","September","October","November","December")
    )
    private lateinit var binding: FragmentTodayBinding
    override fun onResume() {
        (requireActivity() as CoreActivity).binding.floatingActionButton.visibility = View.INVISIBLE
        (requireActivity() as CoreActivity).binding.floatingActionButton.setOnClickListener(null)
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTodayBinding.inflate(inflater,container,false)
        binding.imageView17.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_core)
            navController.popBackStack()
        }
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val now = LocalDate.now()
        val list = mutableListOf<Entry>()
        val tasks = MyApp.preferences.getStringSet("tasks",HashSet<String>())!!
        val today = tasks!!.filter {
            val arr = it.split("|")
            if(arr[0]=="null") return@filter false
            val local = LocalDate.from(formatter.parse(arr[0]))
            return@filter now.isEqual(local)
        }
        val count = today.filter {
            val arr = it.split("|")
            return@filter arr[3]=="complete"
        }.count()
        if(today.isNotEmpty()) binding.circularProgress.setCurrentProgress((count*100)/today.size.toDouble())
        binding.textHome3.text = "$count/${today.size} Tasks"
        binding.circularProgress.maxProgress = 100.0
        binding.chart1.legend.setDrawInside(false)
        binding.chart1.xAxis.setDrawGridLines(false)
        binding.chart1.axisLeft.setDrawGridLines(false)
        binding.chart1.axisRight.setDrawGridLines(false)
        binding.chart1.description.isEnabled = false
        binding.chart1.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.chart1.axisRight.isEnabled = false
        binding.chart1.axisLeft.setDrawAxisLine(false)
        binding.chart1.isHighlightPerTapEnabled = false
        binding.chart1.isHighlightPerDragEnabled = false
        binding.chart1.setScaleEnabled(false)
        binding.chart1.xAxis.setDrawAxisLine(false)
        binding.chart1.axisLeft.setDrawLabels(false)

        binding.chart1.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return months[1][value.toInt()]
            }
        }
        val four: Long = 3
        var tmp = now.minusDays(four)
        var add = 0
        while(add<7) {
            months[0].add(tmp.dayOfMonth.toString())
            list.add(
                Entry(
                    add.toFloat(), tasks.filter {
                        val arr = it.split("|")
                        if(arr[0]=="null") return@filter false
                        val local = LocalDate.from(formatter.parse(arr[0]))
                        return@filter local.isEqual(tmp)
                    }.count().toFloat()
                )
            )
            add++
            tmp = tmp.plusDays(1)
        }
        val data  = LineData()
        val set = LineDataSet(list,null)
        set.setDrawFilled(true)
        set.setDrawCircles(false)
        set.setDrawValues(false)
        binding.chart1.setDrawMarkers(true)
        set.setColor(Color.RED,255)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.1f
        set.fillDrawable = requireContext().getDrawable(R.drawable.grad)
        set.highLightColor = requireActivity().getColor(android.R.color.transparent)
        data.addDataSet(set)
        binding.chart1.data = data

        binding.chart1.highlightValue(3f,0,false)
        binding.chart1.axisLeft.setLabelCount(0,false)
        binding.chart1.animateXY(1000,1000)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}