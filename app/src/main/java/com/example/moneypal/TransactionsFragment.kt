package com.example.moneypal


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.moneypal.Model.solde.SOLDE_COMPTE
import com.example.moneypal.Model.typeTransaction
import com.example.moneypal.recyclerview.item.TransactionTypeItem
import com.example.moneypal.util.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.android.synthetic.main.fragment_transactions.view.*
import org.jetbrains.anko.support.v4.startActivity
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class TransactionsFragment : Fragment() {

    private lateinit var transactionList : ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection : Section

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_transactions, container, false)
        val items = mutableListOf<Item>()
        items.add(TransactionTypeItem(typeTransaction.TRANFERT_RECU))
        items.add(TransactionTypeItem(typeTransaction.TRANFERT_ENVOI))
        items.add(TransactionTypeItem(typeTransaction.FACTURE))
        items.add(TransactionTypeItem(typeTransaction.ACHAT_CREDIT))

        val list : List<Item> = items

        view.recycler_view_type_transaction.apply {
            layoutManager = LinearLayoutManager(this@TransactionsFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = GroupAdapter<ViewHolder>().apply {
                peopleSection = Section(list)
                add(peopleSection)
            }
        }

        view.text_view_solde.text = SOLDE_COMPTE.toString()
        view.text_view_text_solde.text = Calendar.getInstance().time.toString()

        view.btn_more_transactions.setOnClickListener{
            startActivity<TransactionsActivity>()
        }

        transactionList = FirestoreUtil.TransactionListener(this.activity!!, this::updateRecyclerView)
        return view
    }

    private fun updateRecyclerView(items : List<Item>){

        fun init(){

//            recycler_view_last_transaction.apply {
//                layoutManager = LinearLayoutManager(this@TransactionsFragment.context)
//                adapter = GroupAdapter<ViewHolder>().apply {
//                    peopleSection = Section(items)
//                    add(peopleSection)
//                }
//            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if(shouldInitRecyclerView)
            init()
        else
            updateItems()
    }



}
