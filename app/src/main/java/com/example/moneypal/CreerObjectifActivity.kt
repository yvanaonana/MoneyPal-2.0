package com.example.moneypal

import android.icu.util.Calendar
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.example.moneypal.Model.Objectif
import com.example.moneypal.fragment.ModalButtonFragment
import com.example.moneypal.fragment.ParamModalFragment
import com.example.moneypal.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_creer_objectif.*

class CreerObjectifActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creer_objectif)

        btn_add_member.setOnClickListener{
            val myModal = ModalButtonFragment()
            myModal.show(supportFragmentManager, "MODAL")

            //FireStoreUtil.getListOfUser(this, onListen = { createChoisAlertDialog(it) })

            if (ParamModalFragment.listIdUserForGroup.size != 0) {
                Toast.makeText(this, "ok", Toast.LENGTH_LONG).show()
            }
        }
        btn_save_obj.setOnClickListener{
            if (ParamModalFragment.listIdUserForGroup.size != 0)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    FirestoreUtil.addObjectif(Objectif(FirebaseAuth.getInstance().currentUser!!.uid,
                        edit_text_titre_objectif.text.toString(),
                        Integer.parseInt(edit_text_montant_objectif.text.toString()),
                        Calendar.getInstance().time,
                        ParamModalFragment.listIdUserForGroup))
                }
            else {
                val snackbar = Snackbar.make(
                    it,
                    "sélectionnez des membres du groupe",
                    Snackbar.LENGTH_LONG
                )
                snackbar.show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        ParamModalFragment.listIdUserForGroup.clear()
    }
}
