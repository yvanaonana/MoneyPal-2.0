package com.example.moneypal.util

import android.content.Context
import android.util.Log
import com.example.moneypal.Model.Objectif
import com.example.moneypal.Model.Transaction
import com.example.moneypal.Model.User
import com.example.moneypal.recyclerview.TransactionItem
import com.example.moneypal.recyclerview.item.PersonItem
import com.example.moneypal.recyclerview.item.SelectedPersonItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item
import java.lang.NullPointerException

object FirestoreUtil{

    private val firestoreInstance : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef : DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid
            ?: throw NullPointerException ("uid is null")}")

    private val userCollection = firestoreInstance.collection("users")

    private val ObjectifCollection = firestoreInstance.collection("objectif")

    fun initCurrentUserIfFirstTime (onComplete : () -> Unit){
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if(!documentSnapshot.exists()){
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "", null)
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            }

            else
                onComplete()
        }
    }

    fun addTransaction(transaction: Transaction){
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
        userCollection.document(currentUserId)
            .collection("transactions")
            .add(transaction)
    }

//    fun addTransaction(transaction: Transaction, onComplete: (TransactionsId: String) -> Unit){
//        currentUserDocRef.collection("transactions")
//            .document().get().addOnSuccessListener {
//
//                if(it.exists()){
//                    onComplete(it["TransctionsId"] as String)
//                    return@addOnSuccessListener
//                }
//                val newTransaction = transactionCollection.document()
//                newTransaction.set(transaction)
//
//                val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
//
//                currentUserDocRef.collection("transactions")
//                    .document()
//                    .set(mapOf("TransactionsId" to newTransaction.id))
//
//                firestoreInstance.collection("users").document()
//                    .collection("transactions")
//                    .document(currentUserId)
//                    .set(mapOf("channelId" to newTransaction.id))
//
//                onComplete(newTransaction.id)
//            }
//    }

    fun TransactionListener (context : Context, list : (List<Item>) -> Unit) : ListenerRegistration{
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        return firestoreInstance.collection("users").document(currentUserId).collection("transactions")
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "Transaction listener error", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    items.add(TransactionItem(it.toObject(Transaction::class.java)!!))
                }
                list(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()


    fun addSearchUserListenerForcreatingObjectif(valeurRecherche: String, context: Context,
                                               onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error?", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot?.documents?.forEach {
                    if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {

                        if (!valeurRecherche.isEmpty()) {
                            if (it["name"].toString().toUpperCase().contains(valeurRecherche.toUpperCase())
                                || it["bio"].toString().toUpperCase().contains(valeurRecherche.toUpperCase())
                            ) {
                                items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                                Log.d("FIRESTOREUTIL", "NOUVELLE VALEURE AJOUTTEE !!!")
                            }
                        } else {
                            items.add(SelectedPersonItem(it.toObject(User::class.java)!!, it.id, context))
                        }
                    }
                }
                onListen(items)
            }
    }

    fun addObjectif(objectif: Objectif){
        ObjectifCollection.add(objectif)
    }

}