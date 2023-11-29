package com.witask.app.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.witask.app.databinding.CategoryBinding

class CategoriesAdapter(val list: MutableList<Pair<String,Boolean>>,val callback:(s:String)->Unit): RecyclerView.Adapter<CategoriesAdapter.Companion.CategoryHolder>() {


    companion object {
        class CategoryHolder(val binding: CategoryBinding): ViewHolder(binding.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(CategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.binding.checkBox8.text = list[position].first
        holder.binding.checkBox8.isChecked = list[position].second
        holder.binding.checkBox8.setOnCheckedChangeListener { compoundButton, b ->
            list[position] = Pair(list[position].first,b)
            callback(list[position].first)
        }
    }

}