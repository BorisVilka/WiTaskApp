package com.witask.app.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.witask.app.CoreActivity
import com.witask.app.MyApp
import com.witask.app.R
import com.witask.app.databinding.FragmentHomeBinding
import kotlinx.coroutines.awaitAll
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.max

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var ind = 0

    val months = arrayOf(
        mutableListOf(),
        mutableListOf("Su","Mo","Tu","Wed","Th","Fr","Sut"),
        mutableListOf("January","February","March","April","May","June","July","August","September","October","November","December")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
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
        binding.chart1.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val now = LocalDate.now()
                return when(ind) {
                    0 -> {
                        months[0][value.toInt()]
                    }
                    1 -> {
                        months[1][value.toInt()]
                    }
                    else ->  months[2][now.minusMonths(4).plusMonths(value.toLong()).month.value%12].substring(0,3)
                }
            }
        }
        binding.chart1.axisLeft.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return value.toInt().toString()
            }
        }
        val now = LocalDate.now()
        binding.textView4.text = "${now.dayOfMonth} ${months[2][max(0,now.month.value-1)]}, ${now.year}"
        binding.circularProgress.setProgressTextAdapter {
            return@setProgressTextAdapter "${it.toInt()}%"
        }
        binding.button2.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_core)
            navController.navigate(R.id.calendar_fragment)
        }
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                inval(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        inval(0)
        return root
    }

    override fun onResume() {
        (requireActivity() as CoreActivity).binding.floatingActionButton.setImageResource(R.drawable.baseline_add_24)
        (requireActivity() as CoreActivity).binding.floatingActionButton.visibility = View.VISIBLE
        (requireActivity() as CoreActivity).binding.floatingActionButton.setOnClickListener {
           val dialog = TaskDialog() {
               inval(ind)
           }
            dialog.show(requireActivity().supportFragmentManager,"ADD TSK")
        }
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun inval(pos: Int) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val now = LocalDate.now()
        val list = mutableListOf<BarEntry>()
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
        when(pos) {
            2 -> {
                var tmp = now.minusMonths(3)
                var add = 0
                while(add<7) {
                    list.add(
                        BarEntry(
                            add.toFloat(), tasks.filter {
                                val arr = it.split("|")
                                if(arr[0]=="null") return@filter false
                                val local = LocalDate.from(formatter.parse(arr[0]))
                                Log.d("TAG","${local.month} ${tmp.month}")
                                return@filter local.year==tmp.year && local.month==tmp.month
                            }.count().toFloat()
                        )
                    )
                    add++
                    tmp = tmp.plusMonths(1)
                }
            }
            else -> {
                months[0].clear()
                val four: Long = 3
                var tmp = now.minusDays(if(pos==1) now.dayOfWeek.value.toLong() else four)
                var add = 0
                while(add<7) {
                    months[0].add(tmp.dayOfMonth.toString())
                    list.add(
                        BarEntry(
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
            }
        }
        val data  = BarData()
        val set = BarDataSet(list,null)
        set.setDrawValues(false)
        set.setColors(Color.WHITE)
        set.highLightAlpha = 255
        set.highLightColor = requireActivity().getColor(R.color.orange)
        data.addDataSet(set)
        binding.chart1.data = data
        ind = pos
        binding.chart1.highlightValue((if(ind==1) now.dayOfWeek.value else 3).toFloat(),0,false)
        binding.chart1.axisLeft.axisMaximum = list.maxOf { it.y }+1
        binding.chart1.axisLeft.axisMinimum = 0f
        Log.d("TAG","${binding.chart1.axisLeft.axisMaximum}")
        binding.chart1.axisLeft.setLabelCount(max(1,(list.maxOf { it.y }+1).toInt()),false)
        binding.chart1.animateXY(1000,1000)
    }
}