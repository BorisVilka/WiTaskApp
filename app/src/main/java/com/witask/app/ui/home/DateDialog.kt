package com.witask.app.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.fragment.app.DialogFragment
import com.witask.app.MyApp
import com.witask.app.R
import com.witask.app.databinding.AddTaskDialogBinding
import com.witask.app.databinding.CalendarDialogBinding
import java.time.LocalDate

class DateDialog(val callback: (date: LocalDate?)->Unit): DialogFragment() {

    var date: LocalDate? = LocalDate.now()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        val binding = CalendarDialogBinding.inflate(layoutInflater,null,false)
        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            binding.radioGroup.clearCheck()
            Log.d("TAG","$i ${i2 + 1} $i3")
            date = LocalDate.of(i,i2+1,i3)

        }
        binding.radioButton.setOnClickListener {
            date = null
        }
        binding.radioButton2.setOnClickListener {
            date = LocalDate.now()
        }
        binding.radioButton3.setOnClickListener {
            date = LocalDate.now().plusDays(1)
        }
        binding.floatingActionButton3.setOnClickListener {
            callback(date)
            dismiss()
        }
        adb = adb.setView(binding.root)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}