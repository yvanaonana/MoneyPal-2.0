package com.example.moneypal.recyclerview.item

import android.content.Context
import com.bumptech.glide.Glide
import com.example.moneypal.Model.User
import com.example.moneypal.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_person.*


class PersonItem (val person: User,
                  val userId: String,
                  private val context: Context)
    : Item(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text= person.name
        viewHolder.textView_bio.text = person.bio
    }

    override fun getLayout() = R.layout.item_person

}