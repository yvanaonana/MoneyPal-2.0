package com.example.moneypal.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.moneypal.Model.MemberObjectif
import com.example.moneypal.Model.Objectif
import com.example.moneypal.Model.Transaction
import com.example.moneypal.Model.User
import com.example.moneypal.recyclerview.TransactionItem
import com.example.moneypal.recyclerview.item.ObjectifItem
import com.example.moneypal.recyclerview.item.ObjectifMemberItem
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
                    "", 0.0,  null)
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

    fun getUserByUid(uid: String, onComplete: (user: User) -> Unit) {
        userCollection.document(uid).get().addOnSuccessListener { onComplete(it.toObject(User::class.java)!!) }
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

    fun addObjectif(context:Context, objectif: Objectif){
        ObjectifCollection.add(objectif)
            .addOnSuccessListener {
            Toast.makeText(context, "creer avec succes"+it.id, Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "creer avec succes"+it.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun addUserObjectif (context: Context?, list: MutableList<String>, idObjectif: String){
        for (id in list){
            ObjectifCollection.document(idObjectif)
                .collection("members")
                .add(MemberObjectif(id, 0))
                .addOnSuccessListener {
                    Toast.makeText(context, "creer avec succes"+it.id, Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "creer avec succes"+it.toString(), Toast.LENGTH_LONG).show()
                }
        }

    }

    fun updateCurrentUser (name : String = "", bio : String = "", profilePicturePath : String? = null){
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null) userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun deleteTransactions(){

        firestoreInstance.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("transactions")
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "Transaction listener error", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    userCollection.document(FirebaseAuth.getInstance().currentUser!!.uid).collection("transactions").document(it.id).delete()
                }
            }
    }

    fun updateSoldeCompte (solde : Double){
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["solde"] = solde
        currentUserDocRef.update(userFieldMap)
    }

    fun updateMemberContribution (montant:Int, idObjectif: String){
        val memberUpdate = mutableMapOf<String, Any>()
        val objectifUpdate = mutableMapOf<String, Any>()

            ObjectifCollection.document(idObjectif)
                .collection("members").get().addOnSuccessListener {
                    it.documents.forEach {
                    val member = it.toObject(MemberObjectif::class.java)
                    val documentReference :DocumentReference
                    val montF = member?.contribution!! + montant
                    memberUpdate["contribution"] = montF.toString()
                    documentReference = ObjectifCollection.document(idObjectif).collection("members").document(it.id)
                    documentReference.update(memberUpdate)
                        ObjectifCollection.document(idObjectif).get().addOnSuccessListener {
                            val objectif = it.toObject(Objectif::class.java)
                            objectifUpdate["restant"] = objectif!!.restant - montant
                            ObjectifCollection.document(idObjectif).update(objectifUpdate)
                        }
                }

            }

        ObjectifCollection.document(idObjectif).get().addOnSuccessListener {
            val objectif = it.toObject(Objectif::class.java)
            if (objectif?.idAdmin == FirebaseAuth.getInstance().currentUser!!.uid){
                objectifUpdate["restant"] = objectif!!.restant - montant
                ObjectifCollection.document(idObjectif).update(objectifUpdate)
            }
        }
    }

    fun memberObjectifListener (context : Context, idObjectif: String, list : (List<Item>) -> Unit) : ListenerRegistration{
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        return ObjectifCollection.document(idObjectif).collection("members")
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "Transaction listener error", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    val currentMember : MemberObjectif? = it.toObject(MemberObjectif::class.java)
                    var user : User
                    userCollection.document(currentMember!!.idMembre).get().addOnSuccessListener {
                        user = it.toObject(User::class.java)!!
                        items.add(ObjectifMemberItem(user, currentMember.contribution))
                        list(items)
                    }

                }

            }
    }

    fun ObjectifListener(onComplete: (objectifId: String) -> Unit) : ListenerRegistration{


        return ObjectifCollection
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "Objectif listener error", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Objectif>()
                querySnapshot!!.documents.forEach {
                    val currentObjectif = it.toObject(Objectif::class.java!!)
                    ObjectifCollection.document(it.id).collection("members")
                        .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                            if (firebaseFirestoreException != null){
                                Log.e("FIRESTORE", "Members listener error", firebaseFirestoreException)
                                return@addSnapshotListener
                            }

                            querySnapshot!!.documents.forEach {
                                val currentMember = it.toObject(MemberObjectif::class.java)
                                if (currentMember!!.idMembre == FirebaseAuth.getInstance().currentUser?.uid){
                                    items.add(currentObjectif!!)
                                }

                            }
                            onComplete(it.id)
                        }
                }
                items
            }

    }

    fun addObjectifListener (context : Context, list : (List<Item>) -> Unit) : ListenerRegistration{
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        return ObjectifCollection
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null){
                    Log.e("FIRESTORE", "Objectif listener error", firebaseFirestoreException)
                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    val idObjectif = it.id
                    val currentObjectif = it.toObject(Objectif::class.java)
                    if (currentObjectif?.idAdmin == FirebaseAuth.getInstance().currentUser?.uid){
                        items.add(ObjectifItem(currentObjectif!!, idObjectif))
                    }else {

                        ObjectifCollection.document(it.id).collection("members")
                            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                                if (firebaseFirestoreException != null) {
                                    Log.e("FIRESTORE", "Members listener error", firebaseFirestoreException)
                                    return@addSnapshotListener
                                }

                                querySnapshot!!.documents.forEach {
                                    val currentMember = it.toObject(MemberObjectif::class.java)

                                    if (currentMember!!.idMembre == FirebaseAuth.getInstance().currentUser?.uid) {
                                        items.add(ObjectifItem(currentObjectif!!, idObjectif))
                                    }

                                }

                                list(items)
                            }
                    }
                    list(items)
                }
            }
    }


}