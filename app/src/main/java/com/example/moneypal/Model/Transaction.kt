package com.example.moneypal.Model

import java.util.*

object typeTransaction{
    const val TRANFERT_RECU = "TRANSFERT_RECU"
    const val ACHAT_CREDIT = "ACHAT_CREDIT"
    const val TRANFERT_ENVOI = "TRANSFERT_ENVOI"
    const val DEPOT = "DEPOT"
    const val FACTURE = "FACTURE"
    const val RECHARGEMENT = "RECHARGEMENT"
}

object solde{
    var SOLDE_COMPTE = 0
}

data class Transaction(
    val type: String,
    val date: Date,
    val montant: Int
) {
    constructor() : this("", Date(0), 0);
}