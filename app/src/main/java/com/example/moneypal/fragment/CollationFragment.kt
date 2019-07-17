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
import android.content.DialogInterface
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
        var montantString : String = ""


        view.text_view_btn_pay_member.setOnClickListener {
            val view1 = inflater.inflate(R.layout.text_view_layout, null)
            val a = AlertDialog.Builder(this@CollationFragment.context)
                .setMessage("bonjour")
                .setView(view1)
                .setNegativeButton("cancel", null)
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                    montantString = view1.edit_text_input_text.text.toString()
                    val i :Intent = HoverParameters.Builder(this@CollationFragment.context)
                        .request("200069fb")
                        .extra("montant", montantString)
                        .extra("codeSecret", 1405.toString())
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
        } else if (requestCode == 0 && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this@CollationFragment.context, "Error: " + data!!.getStringExtra("error"), Toast.LENGTH_LONG).show()
        }
    }


}
