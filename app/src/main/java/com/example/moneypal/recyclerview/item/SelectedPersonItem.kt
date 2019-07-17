package com.example.moneypal.recyclerview.item

import android.content.Context
import com.example.moneypal.Model.User
import com.example.moneypal.R
import com.example.moneypal.fragment.ParamModalFragment
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.textView_bio
import kotlinx.android.synthetic.main.item_person.textView_name
import kotlinx.android.synthetic.main.item_persone_create_groupe.*
import org.jetbrains.anko.toast

class SelectedPersonItem (val person: User,
                          val userIdFirebase: String,
                          private val context: Context
): Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio

        viewHolder.id_chekbox.setOnCheckedChangeListener{buttonView, ischecked->
            if(ischecked){
                context.toast("utilisateur ajouter: "+userIdFirebase)
                ParamModalFragment.listIdUserForGroup.add(userIdFirebase)
            }else{
                context.toast("utilisateur retirer: "+userIdFirebase)
                ParamModalFragment.listIdUserForGroup.remove(userIdFirebase)
            }
        }

    }
    override fun getLayout()= R.layout.item_persone_create_groupe
}