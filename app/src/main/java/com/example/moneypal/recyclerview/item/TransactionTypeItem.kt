package com.example.moneypal.recyclerview.item

import com.example.moneypal.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transaction_type.*

class TransactionTypeItem (val type : String) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text_view_type_transaction.text = type
    }

    override fun getLayout() = R.layout.item_transaction_type
}