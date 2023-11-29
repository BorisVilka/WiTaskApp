package com.witask.app.ui.categories

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.witask.app.databinding.NewCategoryDialogBinding

class CategoryDialog(val s1:String?,val callBack: (s: String)-> Unit): DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        val binding = NewCategoryDialogBinding.inflate(layoutInflater,null,false)
        if(s1!=null) binding.text.setText(s1)
        binding.textView8.setOnClickListener {
            dismiss()
        }
        binding.textView7.setOnClickListener {
            callBack(binding.text.text.toString())
            dismiss()
        }
        adb = adb.setView(binding.root)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}