package com.witask.app.ui.dashboard

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import com.witask.app.R
import com.witask.app.databinding.ChildItemBinding
import com.witask.app.databinding.GroupItemBinding
import java.time.format.DateTimeFormatter

class ExpandableAdapter(var data: Array<MutableList<String>>, val manager: RecyclerViewExpandableItemManager,val callback: (s:String)->Unit):
    AbstractExpandableItemAdapter<ExpandableAdapter.Companion.GroupHolder, ExpandableAdapter.Companion.ItemHolder>() {

    companion object {
        class GroupHolder(val binding: GroupItemBinding): AbstractExpandableItemViewHolder(binding.root) {


        }
        class ItemHolder(val binding: ChildItemBinding): AbstractExpandableItemViewHolder(binding.root) {

        }
    }

    fun setNewData(data1: Array<MutableList<String>>) {
        this.data = data1
        notifyDataSetChanged()
    }

    override fun getGroupCount(): Int {
        return data.size
    }

    override fun getChildCount(groupPosition: Int): Int {
        return data[groupPosition].size
    }

    override fun getGroupId(groupPosition: Int): Long {
       return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return (Math.pow(10.0,groupPosition.toDouble())+childPosition).toLong()
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): GroupHolder {
        return GroupHolder(GroupItemBinding.inflate(LayoutInflater.from(parent!!.context),parent,false))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): ItemHolder {
        return ItemHolder(ChildItemBinding.inflate(LayoutInflater.from(parent!!.context),parent,false))
    }

    override fun onCheckCanExpandOrCollapseGroup(
        holder: GroupHolder,
        groupPosition: Int,
        x: Int,
        y: Int,
        expand: Boolean
    ): Boolean {
        return true
    }

    override fun onBindChildViewHolder(
        holder: ItemHolder,
        groupPosition: Int,
        childPosition: Int,
        viewType: Int
    ) {
        val arr = data[groupPosition][childPosition].split("|")
        holder.binding.textView13.text = arr[1]
        if(arr[2]!="null") holder.binding.textView14.text = arr[2]
        val format = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val parse = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        if(arr[0]=="null") {
            holder.binding.textView15.text = "none"
        } else {
            holder.binding.textView15.text = format.format(parse.parse(arr[0]))
        }
        if(arr[3]!="complete") {
            holder.binding.checkBox10.setOnCheckedChangeListener { compoundButton, b ->
                if(b) {
                    val tmp = data[groupPosition].removeAt(childPosition)
                    data[2].add(tmp)
                    manager.notifyChildItemRemoved(groupPosition,childPosition)
                    manager.notifyChildItemInserted(2,data[2].size-1)
                    manager.notifyGroupItemChanged(2)
                    manager.notifyGroupItemChanged(groupPosition)
                    callback(tmp)
                }
            }
        } else {
            holder.binding.textView13.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            holder.binding.checkBox10.isChecked = true
            holder.binding.checkBox10.isEnabled = false
        }
    }

    override fun onBindGroupViewHolder(holder: GroupHolder, groupPosition: Int, viewType: Int) {
       holder.binding.imageView2.text = data[groupPosition].size.toString()
        holder.binding.itemNumber.text = arrayOf("Previous","Today","Complete")[groupPosition]
        if(holder.expandState.isUpdated) {
            if(holder.expandState.isExpanded) {
                holder.binding.catNav2.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
            } else holder.binding.catNav2.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
        }
    }
}