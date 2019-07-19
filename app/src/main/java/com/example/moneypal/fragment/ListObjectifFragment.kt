package com.example.moneypal.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.moneypal.R
import com.example.moneypal.recyclerview.item.ObjectifItem
import com.example.moneypal.util.FirestoreUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_list_objectif.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListObjectifFragment : BottomSheetDialogFragment() {

    private lateinit var objectifListener: ListenerRegistration
    private var shouldInitrecycleView = true
    private lateinit var poepleSection: Section
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        objectifListener = FirestoreUtil.addObjectifListener(this.activity!!, this::updateRecycleView)
        return inflater.inflate(R.layout.fragment_list_objectif, container, false)
    }

    private fun updateRecycleView(items: List<Item>) {

        fun init() {
            recycle_view_objectif.apply {
                layoutManager = LinearLayoutManager(this@ListObjectifFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    poepleSection = Section(items)
                    add(poepleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitrecycleView = false
        }

        fun updateItems() = poepleSection.update(items)

        if (shouldInitrecycleView)
            init()
        else
            updateItems()

    }

    private val onItemClick = OnItemClickListener { item, view ->
        Toast.makeText(this@ListObjectifFragment.context, "click", Toast.LENGTH_LONG).show()
        if (item is ObjectifItem){
            ParamModalFragment.objectifItem = item
            Toast.makeText(this@ListObjectifFragment.context, "click if", Toast.LENGTH_LONG).show()
            onDestroyView()
        }
    }
}
