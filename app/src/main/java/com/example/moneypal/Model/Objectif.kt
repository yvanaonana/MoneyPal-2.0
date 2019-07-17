package com.example.moneypal.Model

import java.util.*

class Objectif (val idAdmin:String, val titre:String, val montant:Int, val DateCreation: Date, val idMembers : MutableList<String>?) {

    constructor() : this("", "", 0, Date(0), null)

}