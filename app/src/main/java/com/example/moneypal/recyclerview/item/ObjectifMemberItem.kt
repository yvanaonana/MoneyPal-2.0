package com.example.moneypal.recyclerview.item

import com.example.moneypal.Model.User
import com.example.moneypal.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transaction.*

class ObjectifMemberItem (val user: User, val montant:Int) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text_view_montant_transaction.text = montant.toString()
        viewHolder.text_view_nom_transaction.text = user.name
        viewHolder.text_view_date_transaction.text = ""
    }

    override fun getLayout()= R.layout.item_transaction

}