package com.example.moneypal.recyclerview.item

import com.example.moneypal.Model.Objectif
import com.example.moneypal.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transaction.*



class ObjectifItem(val objectif: Objectif, val idObjectif:String) : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text_view_montant_transaction.text = objectif.montant.toString()
        viewHolder.text_view_nom_transaction.text = objectif.titre
        viewHolder.text_view_date_transaction.text = ""
    }

    override fun getLayout() = R.layout.item_transaction
}