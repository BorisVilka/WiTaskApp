package com.witask.app.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.witask.app.databinding.CalendarItemBinding

class CalendarAdapter(val list: MutableList<String>): RecyclerView.Adapter<CalendarAdapter.Companion.CalendarHolder>() {


    fun setNewData(data: MutableList<String>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    companion object {
        class CalendarHolder(val binding: CalendarItemBinding): ViewHolder(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        return CalendarHolder(CalendarItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
       val arr =list[position].split("|")
        holder.binding.textView13.text = arr[1]
        if(arr[2]!="null") holder.binding.textView14.text = arr[2]
    }
}