package com.witask.app.ui.dashboard

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
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import com.witask.app.CoreActivity
import com.witask.app.MyApp
import com.witask.app.R
import com.witask.app.databinding.FragmentDashboardBinding
import com.witask.app.ui.categories.CategoryDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.work.WorkManager

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val now = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var tasks = MyApp.preferences.getStringSet("tasks",HashSet<String>())!!
        var today = tasks.filter {
            val arr = it.split("|")
            if(arr[0]=="null") return@filter false
            val local = LocalDate.from(formatter.parse(arr[0]))
            return@filter now.isEqual(local) && arr[3]=="ready"
        }.toMutableList()
        var prev = tasks.filter {
            val arr = it.split("|")
            if(arr[0]=="null") return@filter true
            val local = LocalDate.from(formatter.parse(arr[0]))
            return@filter local.isBefore(now) && arr[3]=="ready"
        }.toMutableList()
        var compl = tasks.filter {
            val arr = it.split("|")
            return@filter arr[3]=="complete"
        }.toMutableList()
        val manager = RecyclerViewExpandableItemManager(null)
        val adapter1 = ExpandableAdapter(arrayOf(prev,today, compl),manager) {
            val list = tasks.toMutableList()
            var i = 0
            while(i<list.size) {
                if(list[i]==it) {
                    list.removeAt(i)
                    break
                }
                i++
            }
            val arr = it.split("|")
            list.add("${arr[0]}|${arr[1]}|${arr[2]}|complete")
            Log.d("TAG",list.toString())
            WorkManager.getInstance(requireContext()).cancelUniqueWork(it)
            MyApp.preferences.edit().putStringSet("tasks",list.toSet()).apply()
        }
        adapter1.setHasStableIds(true)
        val adp = manager.createWrappedAdapter(adapter1)
        val animator = RefactoredDefaultItemAnimator()
        animator.supportsChangeAnimations = false
        binding.expand.adapter = adp
        binding.expand.itemAnimator = animator
        binding.expand.setHasFixedSize(false)
        manager.attachRecyclerView(binding.expand)
        val set = MyApp.preferences.getStringSet("categories",HashSet<String>())
        val list = set!!.toMutableList().apply { add(0,"All") }.map { Pair(it,false) }.toMutableList()
        val adapter = CategoriesAdapter(list) {
            var tmp = list.filter { it.second }.map { it.first }.toList()
            if(list[0].second || tmp.isEmpty()) {
                tasks = MyApp.preferences.getStringSet("tasks",HashSet<String>())!!
                today = tasks.filter {
                    val arr = it.split("|")
                    if(arr[0]=="null") return@filter false
                    val local = LocalDate.from(formatter.parse(arr[0]))
                    return@filter now.isEqual(local) && arr[3]=="ready"
                }.toMutableList()
                prev = tasks.filter {
                    val arr = it.split("|")
                    if(arr[0]=="null") return@filter true
                    val local = LocalDate.from(formatter.parse(arr[0]))
                    return@filter local.isBefore(now) && arr[3]=="ready"
                }.toMutableList()
                compl = tasks.filter {
                    val arr = it.split("|")
                    return@filter arr[3]=="complete"
                }.toMutableList()
                adapter1.setNewData(arrayOf(prev,today, compl))
            } else {
                tasks = MyApp.preferences.getStringSet("tasks",HashSet<String>())!!
                today = tasks.filter {
                    val arr = it.split("|")
                    if(arr[0]=="null") return@filter false
                    val local = LocalDate.from(formatter.parse(arr[0]))
                    return@filter now.isEqual(local) && tmp.contains(arr[2]) && arr[3]=="ready"
                }.toMutableList()
                prev = tasks.filter {
                    val arr = it.split("|")
                    return@filter(arr[0]=="null" ||  LocalDate.from(formatter.parse(arr[0])).isBefore(now)) && tmp.contains(arr[2]) && arr[3]=="ready"
                }.toMutableList()
                compl = tasks.filter {
                    val arr = it.split("|")
                    return@filter arr[3]=="complete" && tmp.contains(arr[2])
                }.toMutableList()
                adapter1.setNewData(arrayOf(prev,today, compl))
            }
        }
        binding.categories.adapter = adapter
        binding.catNav.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_core)
            navController.navigate(R.id.categoryFragment)
        }
        return binding.root
    }
    override fun onResume() {
        (requireActivity() as CoreActivity).binding.floatingActionButton.setImageResource(R.drawable.baseline_add_24)
        (requireActivity() as CoreActivity).binding.floatingActionButton.visibility = View.VISIBLE
        (requireActivity() as CoreActivity).binding.floatingActionButton.setOnClickListener {
            val dialog = CategoryDialog(null) {
               val set = MyApp.preferences.getStringSet("categories",HashSet<String>())
               val set1 = HashSet<String>()
               set1.addAll(set!!)
               if(!set1.contains(it)) set1.add(it)
                MyApp.preferences.edit().putStringSet("categories",set1).apply()
            }
            dialog.show((activity as CoreActivity).supportFragmentManager,"ADD CATEGORY")
        }
        super.onResume()
    }
}