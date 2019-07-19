package com.example.moneypal.Model

import java.util.*

class Objectif (val idAdmin:String, val telephoneAdmin:String, val titre:String, val montant:Int, val restant:Int, val DateCreation: Date) {

    constructor() : this("", "", "", 0, 0,  Date(0))

}