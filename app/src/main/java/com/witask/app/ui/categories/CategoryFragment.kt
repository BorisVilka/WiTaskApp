package com.witask.app.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.witask.app.MyApp
import com.witask.app.R
import com.witask.app.databinding.FragmentCategoryListBinding
import com.witask.app.ui.categories.placeholder.PlaceholderContent

/**
 * A fragment representing a list of Items.
 */
class CategoryFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    private lateinit var binding: FragmentCategoryListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryListBinding.inflate(inflater,container,false)
        val set = MyApp.preferences.getStringSet("categories",HashSet<String>())
        val list = set!!.toMutableList()
        binding.list.adapter = CategoryAdapter(list,requireActivity())
        binding.imageView3.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_core)
            navController.popBackStack()
        }
        return binding.root
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}