package com.example.moneypal.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.moneypal.R
import com.example.moneypal.util.FirestoreUtil
import com.google.android.gms.appinvite.AppInviteInvitation
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.appinvite.FirebaseAppInvite
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_conversation.*
import kotlinx.android.synthetic.main.fragment_conversation.view.*


class ModalButtonFragment() : BottomSheetDialogFragment() {
    private lateinit var userListenerRegistration: ListenerRegistration
    private var shouldInitrecycleView = true
    private lateinit var poepleSection: Section

    private val REQUEST_INVITE: Int = 4
    // cet objet va contenir la liste des mebres du groupe qui auront été sélectionnés à la création du groupe
    private val memberOfGroupe = arrayListOf<ModalSelectedMember>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_conversation, container, false)

        userListenerRegistration = FirestoreUtil.addSearchUserListenerForcreatingObjectif("",
            this@ModalButtonFragment.context!!
            ,
            onListen = {
                updateRecycleView(it)
            }
        )

//        if (Intent.getIntent(getString(R.string.invitation_deep_link))!=null) {
//
//            FirebaseDynamicLinks.getInstance()
//                .getDynamicLink(Intent.getIntent(getString(R.string.invitation_deep_link)))
//                .addOnSuccessListener(this.activity!!, OnSuccessListener { data ->
//                    if (data == null) {
//                        Log.d(TAG, "getInvitation: no data")
//                        return@OnSuccessListener
//                    }
//
//                    // Get the deep link
//                    val deepLink = data.link
//
//                    // Extract invite
////                    val invite = FirebaseAppInvite.getInvitation(data)
////                    val invitationId = invite.invitationId
////                    Log.d(TAG, "getInvitation: data")
//                    Toast.makeText(this@ModalButtonFragment.context, "succes", Toast.LENGTH_LONG).show()
//
//                    // Handle the deep link
//                    // ...
//                })
//                .addOnFailureListener(this.activity!!) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
//        }
        view.btn_inviter_user.setOnClickListener {
            onInviteClicked()
        }
        ParamModalFragment.listIdUserForGroup.clear()
        return view
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroyView() {
        super.onDestroy()
        FirestoreUtil.removeListener(userListenerRegistration)
        shouldInitrecycleView = true
    }


    private fun updateRecycleView(items: List<Item>) {

        fun init() {
            recycle_view_peaple.apply {
                layoutManager = LinearLayoutManager(this@ModalButtonFragment.context)
                adapter = GroupAdapter<ViewHolder>().apply {
                    poepleSection = Section(items)
                    add(poepleSection)
                    setOnItemClickListener(onItemClick)
                }
            }
            shouldInitrecycleView = false
        }

        fun updateItems() = poepleSection.update(items)

        if (shouldInitrecycleView)
            init()
        else
            updateItems()

    }
    private val onItemClick = OnItemClickListener { item, view ->

    }

    private fun onInviteClicked() {
//        val intent = AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
//            .setMessage(getString(R.string.invitation_message))
//            .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//            .setCallToActionText(getString(R.string.invitation_cta))
//            .build()
//        startActivityForResult(intent, REQUEST_INVITE)

        val dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLongLink(Uri.parse("https://yvana.page.link/?link=https://drive.google.com/open?id%3D1bSBz47n-uKv_34--BTICQYJ-s16dPJT7"))
            .setLink(Uri.parse("https://yvana.page.link/MoneyPal"))
            .setDomainUriPrefix("https://yvana.page.link")
            // Open links with this app on Android
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .buildDynamicLink()

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT, getString(R.string.invitation_message) +
                    "\n\n" + dynamicLink.uri
        )
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Let's Learn Miwok!")
        sendIntent.type = "text/plain"
        startActivity(
            Intent.createChooser(
                sendIntent,
                resources.getText(R.string.menu_send)
            )
        )
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        Log.d(TAG, "onActivityResult: requestCode=$requestCode, resultCode=$resultCode")
//
//        if (requestCode == REQUEST_INVITE) {
//            if (resultCode == Activity.RESULT_OK) {
//                // Get the invitation IDs of all sent messages
//                val ids = AppInviteInvitation.getInvitationIds(resultCode, data!!)
//                for (id in ids) {
//                    Log.d(TAG, "onActivityResult: sent invitation $id")
//                }
//            } else {
//                // Sending failed or it was canceled, show failure message to the user
//                // ...
//            }
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        //ParamModalFragment.listIdUserForGroup.clear()
        for (tmp_item in this.memberOfGroupe) {
            //ParamModalFragment.listIdUserForGroup.add(tmp_item.uidSelectedMember)
        }
    }

    data class ModalSelectedMember(var uidSelectedMember: String, var view: View)

}