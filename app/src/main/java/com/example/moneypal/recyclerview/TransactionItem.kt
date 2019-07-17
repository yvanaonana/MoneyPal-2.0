package com.example.moneypal.recyclerview

import com.example.moneypal.Model.Transaction
import com.example.moneypal.Model.typeTransaction
import com.example.moneypal.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transaction.*
import org.jetbrains.anko.textColor

class TransactionItem (val transaction : Transaction) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text_view_nom_transaction.text = transaction.type
        viewHolder.text_view_date_transaction.text = transaction.date.toString()
        setMontantTransaction(viewHolder)
    }

    fun setMontantTransaction (viewHolder: ViewHolder){
        viewHolder.text_view_montant_transaction.text = transaction.montant.toString()
        if (transaction.type == typeTransaction.ACHAT_CREDIT)
            viewHolder.text_view_montant_transaction.textColor = R.color.tranfert
        if (transaction.type == typeTransaction.FACTURE)
            viewHolder.text_view_montant_transaction.textColor = R.color.facture
        if (transaction.type == typeTransaction.TRANFERT_ENVOI)
            viewHolder.text_view_montant_transaction.textColor = R.color.tranfert
        if (transaction.type == typeTransaction.TRANFERT_RECU)
            viewHolder.text_view_montant_transaction.textColor = R.color.recu
        if (transaction.type == typeTransaction.DEPOT)
            viewHolder.text_view_montant_transaction.textColor = R.color.recu
    }

    override fun getLayout() = R.layout.item_transaction
}