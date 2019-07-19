package com.example.moneypal.Model

data class User (val name : String, val bio : String, val solde : Double,  val profilePicturePath : String?) {

    constructor() : this("", "", 0.0, null)

}