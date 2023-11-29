package com.witask.app.ui.categories

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import com.witask.app.CoreActivity
import com.witask.app.MyApp
import com.witask.app.R

import com.witask.app.ui.categories.placeholder.PlaceholderContent.PlaceholderItem
import com.witask.app.databinding.CategotyItemBinding

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CategoryAdapter(
    private val values: MutableList<String>,
    val activity: Activity
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CategotyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(holder.itemViewType==1) {
            holder.binding.catNav2.visibility = View.INVISIBLE
            holder.binding.imageView2.setImageResource(R.drawable.baseline_add_24)
            holder.binding.root.setOnClickListener {
                val dialog = CategoryDialog(null) {
                    values.add(0,it)
                   /// val list = mutableListOf<String>()
                   // list.addAll(values)
                  //  list.removeAt(list.size-1)
                    MyApp.preferences.edit().putStringSet("categories",values.toSet()).apply()
                   notifyDataSetChanged()
                }
                dialog.show((activity as CoreActivity).supportFragmentManager,"ADD CATEGORY")
            }
        } else {
            holder.binding.itemNumber.text = values[position]
            holder.binding.imageView2.setImageResource(R.drawable.circle)
            holder.binding.catNav2.setOnClickListener {
                val menu = PopupMenu(activity,holder.binding.catNav2)
                menu.inflate(R.menu.popup)
                menu.setOnMenuItemClickListener { it1 ->
                    if(it1.itemId==R.id.delete) {
                        values.removeAt(position)
                        val list = mutableListOf<String>()
                        list.addAll(values)
                        list.removeAt(list.size-1)
                        MyApp.preferences.edit().putStringSet("categories",values.toSet()).apply()
                        notifyDataSetChanged()
                    } else {
                        val dialog = CategoryDialog(values[position]) {
                            values[position] = it
                            val list = mutableListOf<String>()
                            list.addAll(values)
                            list.removeAt(list.size-1)
                            MyApp.preferences.edit().putStringSet("categories",values.toSet()).apply()
                            notifyDataSetChanged()
                        }
                        dialog.show((activity as CoreActivity).supportFragmentManager,"CHANGE CATEGORY")
                    }
                    return@setOnMenuItemClickListener true
                }
                menu.show()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==values.size) 1 else 0
    }

    override fun getItemCount(): Int = values.size+1

    inner class ViewHolder(val binding: CategotyItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

}