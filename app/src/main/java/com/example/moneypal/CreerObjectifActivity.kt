package com.example.moneypal

import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.widget.Toast
import com.example.moneypal.Model.Objectif
import com.example.moneypal.fragment.ModalButtonFragment
import com.example.moneypal.fragment.ParamModalFragment
import com.example.moneypal.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_creer_objectif.*
import java.time.Instant
import java.util.*

class CreerObjectifActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creer_objectif)

        btn_save_obj.setOnClickListener{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FirestoreUtil.addObjectif(this, Objectif(FirebaseAuth.getInstance().currentUser!!.uid,
                    edit_text_telephone.text.toString(),
                    edit_text_titre_objectif.text.toString(),
                    Integer.parseInt(edit_text_montant_objectif.text.toString()),
                    Integer.parseInt(edit_text_montant_objectif.text.toString()),
                    Calendar.getInstance().time))
            }else{
                FirestoreUtil.addObjectif(this, Objectif(FirebaseAuth.getInstance().currentUser!!.uid,
                    edit_text_telephone.text.toString(),
                    edit_text_titre_objectif.text.toString(),
                    Integer.parseInt(edit_text_montant_objectif.text.toString()),
                    Integer.parseInt(edit_text_montant_objectif.text.toString()),
                    Date(0)))
            }

        }
    }

}
