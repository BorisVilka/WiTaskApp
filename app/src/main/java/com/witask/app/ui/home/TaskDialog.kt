package com.witask.app.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.witask.app.MyApp
import com.witask.app.MyWorker
import com.witask.app.R
import com.witask.app.databinding.AddTaskDialogBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Random
import java.util.concurrent.TimeUnit

class TaskDialog(val callBack: ()->Unit): DialogFragment() {

    var category: String? = "null"
    var date : LocalDate? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        val binding = AddTaskDialogBinding.inflate(layoutInflater,null,false)
        binding.catNav3.setOnClickListener {
            val dateDialog = DateDialog {
                if(it!=null) date = LocalDate.from(it)
            }
            dateDialog.show(requireActivity().supportFragmentManager,"DATE")
        }
        binding.button3.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity(),binding.button3)
            popupMenu.inflate(R.menu.empty)
            val set = MyApp.preferences.getStringSet("categories",HashSet<String>())
            val list = set!!.toMutableList()
            for(i in list) popupMenu.menu.add(i)
            popupMenu.setOnMenuItemClickListener {
                category = it.title.toString()
                Log.d("TAG",category!!)
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
        binding.floatingActionButton2.setOnClickListener {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val task = "${if(date!=null) formatter.format(date) else "null"}|${binding.text.text.toString()}|${category}|ready"
            val set = MyApp.preferences.getStringSet("tasks",HashSet<String>())
            val set1 = HashSet<String>()
            set1.addAll(set!!)
            set1.add(task)
            MyApp.preferences.edit().putStringSet("tasks",set1).apply()
            if(date!=null) {
                val data = Data.Builder()
                    .putString("title",category)
                    .putString("message",binding.text.text.toString())
                    .putInt("id", Random().nextInt(Integer.MAX_VALUE))
                val request = OneTimeWorkRequestBuilder<MyWorker>()
                    .setInitialDelay(
                        ChronoUnit.DAYS.between(date,LocalDate.now()),
                        TimeUnit.DAYS)
                    .setInputData(data.build())
                WorkManager.getInstance(requireActivity()).enqueueUniqueWork(task,ExistingWorkPolicy.REPLACE,request.build())
            }
            dismiss()
            callBack()
        }
        adb = adb.setView(binding.root)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}