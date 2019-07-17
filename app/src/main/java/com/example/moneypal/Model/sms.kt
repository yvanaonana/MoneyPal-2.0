package com.example.moneypal.Model

import java.util.*

data class sms (val message : String, val date: Date){

    constructor() : this ("", Date(0))
}