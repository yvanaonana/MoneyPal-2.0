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
            val soldeString : String? = Regex(pattern = """\d+""").find(input = message.substring(message.indexOf("olde")))?.value
            FirestoreUtil.updateSoldeCompte((soldeString!!.toDouble()))
            return typeTransaction.RECHARGEMENT
        }else if(Regex(pattern = "Transfert").containsMatchIn(input = message)) {
            val soldeString : String? = Regex(pattern = """\d+""").find(input = message.substring(message.indexOf("olde")))?.value
            FirestoreUtil.updateSoldeCompte((soldeString!!.toDouble()))
            return typeTransaction.TRANFERT_ENVOI
        }else if(Regex(pattern = "Depot effectue").containsMatchIn(input = message)){
            val soldeString : String? = Regex(pattern = """\d+""").find(input = message.substring(message.indexOf("olde")))?.value
            FirestoreUtil.updateSoldeCompte((soldeString!!.toDouble()))
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