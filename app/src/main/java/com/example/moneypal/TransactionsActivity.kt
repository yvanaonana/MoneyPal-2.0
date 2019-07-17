package com.example.moneypal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.moneypal.util.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_transactions.*

class TransactionsActivity : AppCompatActivity() {

    private lateinit var transactionList : ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection : Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        transactionList = FirestoreUtil.TransactionListener(this, this::updateRecyclerView)
    }

    private fun updateRecyclerView(items : List<Item>){

        fun init(){

            recycler_view_all_transaction.apply {
                layoutManager = LinearLayoutManager(this@TransactionsActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if(shouldInitRecyclerView)
            init()
        else
            updateItems()
    }
}
