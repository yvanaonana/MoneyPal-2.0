package com.example.moneypal.util

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.example.moneypal.Model.Transaction
import com.example.moneypal.Model.solde.SOLDE_COMPTE
import com.example.moneypal.Model.typeTransaction
import java.util.*

object MessagesUtil {

    fun determinerTypeMessage (message:String) : String{
        if (Regex(pattern = "Rechargement reussi").containsMatchIn(input = message)){
            return typeTransaction.RECHARGEMENT
        }else if(Regex(pattern = "Transaction reussi").containsMatchIn(input = message)) {
            return typeTransaction.TRANFERT_RECU
        }else if(Regex(pattern = "Depot effectue").containsMatchIn(input = message)){
            return typeTransaction.DEPOT
        }
        else
            return "NON PRIS EN CHARGE"
    }

    fun traiterMessage (message: String, typeTransaction:String, date: Date){
        if (!typeTransaction.equals("NON PRIS EN CHARGE")){
            try{
                val montantString : String? = Regex(pattern = """\d+""").find(input = message.substring(message.indexOf("ontant")))?.value
                val montant = Integer.parseInt(montantString)

                val soldeString : String? = Regex(pattern = """\d+""").find(input = message.substring(message.indexOf("olde")))?.value
                val solde = Integer.parseInt(soldeString)
                SOLDE_COMPTE = solde

                FirestoreUtil.addTransaction(Transaction(typeTransaction, date, montant))
            }catch (e:Exception){
                Log.e(TAG, "erreur lecture message")
            }
        }
        else{
            Log.e(TAG, "format non pris en charge")
        }
    }

}