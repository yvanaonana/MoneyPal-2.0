package com.example.moneypal.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moneypal.CreerObjectifActivity

import com.example.moneypal.R
import com.hover.sdk.api.HoverParameters
import kotlinx.android.synthetic.main.activity_creer_objectif.view.*
import kotlinx.android.synthetic.main.fragment_collation.view.*
import org.jetbrains.anko.support.v4.startActivity
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent.getIntent
import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moneypal.MoneyPalActivity
import com.example.moneypal.util.FirestoreUtil
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.appinvite.FirebaseAppInvite
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_collation.*
import kotlinx.android.synthetic.main.fragment_collation.view.recycler_view_collation
import kotlinx.android.synthetic.main.text_view_layout.view.*
import java.lang.Exception


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CollationFragment : Fragment() {


    private lateinit var MemberList : ListenerRegistration

    private var shouldInitRecyclerView = true

    private lateinit var peopleSection : Section
    var montantString : String = ""
    var currentObjectifId : String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collation, container, false)
        view.btn_add_collation.setOnClickListener{
            val i = Intent(this@CollationFragment.context, CreerObjectifActivity::class.java)
            startActivity(i)
        }

        var objectifItem = ParamModalFragment.objectifItem


        if(objectifItem!=null){
            currentObjectifId = objectifItem!!.idObjectif

            view.text_view_objet_collation.text = objectifItem.objectif.titre
            view.text_view_text_solde_collation.text = objectifItem.objectif.DateCreation.toString()
            view.text_view_solde_collation.text = objectifItem.objectif.montant.toString() +"FCFA"
            val i = (objectifItem.objectif.montant-objectifItem.objectif.restant)*(100/objectifItem.objectif.montant)
            view.progress_bar_niveau_collation.setProgress(i)

            MemberList = FirestoreUtil.memberObjectifListener(this.activity!!, currentObjectifId,  this::updateRecyclerView)

            view.text_view_btn_add_member.setOnClickListener {
                val myModal = ModalButtonFragment()
                myModal.show(fragmentManager, "MODAL")

                //FireStoreUtil.getListOfUser(this, onListen = { createChoisAlertDialog(it) })

                if (ParamModalFragment.listIdUserForGroup.size != 0) {
                    Toast.makeText(this@CollationFragment.context, "ok", Toast.LENGTH_LONG).show()
                    FirestoreUtil.addUserObjectif(this@CollationFragment.context,
                        ParamModalFragment.listIdUserForGroup,
                        ParamModalFragment.objectifItem!!.idObjectif)
                }
            }

        }else{
            val obj = ListObjectifFragment()
            obj.show(fragmentManager, "OBJ")
        }

        view.btn_list_collation.setOnClickListener {
            val obj = ListObjectifFragment()
            obj.show(fragmentManager, "OBJ")
        }

        view.text_view_btn_pay_member.setOnClickListener {
            val view1 = inflater.inflate(R.layout.text_view_layout, null)
            val a = AlertDialog.Builder(this@CollationFragment.context)
                .setTitle("Transaction")
                .setView(view1)
                .setNegativeButton("cancel", null)
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                    montantString = view1.edit_text_input_text.text.toString()
                    val i :Intent = HoverParameters.Builder(this@CollationFragment.context)
                        .request("7ee6c644")
                        .extra("montant", montantString)
                        .extra("numeroTelephone", ParamModalFragment.objectifItem?.objectif?.telephoneAdmin)
                        .buildIntent();
                    startActivityForResult(i, 0);
                })
                .create()
            a.show()

        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val sessionTextArr = data!!.getStringArrayExtra("ussd_messages")
            val uuid = data.getStringExtra("uuid")
            Toast.makeText(this@CollationFragment.context, sessionTextArr.toString(), Toast.LENGTH_LONG).show()
            FirestoreUtil.updateMemberContribution(Integer.parseInt(montantString), currentObjectifId)
        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this@CollationFragment.context, "Error: " + data!!.getStringExtra("error"), Toast.LENGTH_LONG).show()
        }
    }


    private fun updateRecyclerView(items : List<Item>){

        fun init(){

            recycler_view_collation.apply {
                layoutManager = LinearLayoutManager(this@CollationFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    peopleSection = Section(items)
                    add(peopleSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = peopleSection.update(items)

        if(shouldInitRecyclerView)
            init()
        else
            updateItems()
    }
}
