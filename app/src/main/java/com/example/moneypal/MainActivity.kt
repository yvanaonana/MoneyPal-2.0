package com.example.moneypal

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.example.moneypal.Model.Transaction
import com.example.moneypal.Model.sms
import com.example.moneypal.Model.typeTransaction
import com.example.moneypal.util.FirestoreUtil
import com.example.moneypal.util.MessagesUtil
import org.jetbrains.anko.startActivity
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val requestReadSMS :Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), requestReadSMS)
        }else{
            readSMS()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == requestReadSMS) readSMS()
    }

    private fun readSMS() {

        val SMSList = ArrayList<sms>()

        val cursor = contentResolver.query(
            Uri.parse("content://sms/inbox"),
            null,
            "address = 'OrangeMoney'",
            null,
            "date DESC"
        )

        if (cursor.moveToFirst()){
            val messageID = cursor.getColumnIndex("body")
            val dateID = cursor.getColumnIndex("date")

            do {
                val dateString = cursor.getString(dateID)
                val type = MessagesUtil.determinerTypeMessage(cursor.getString(messageID))
                MessagesUtil.traiterMessage(cursor.getString(messageID), type, Date(dateString.toLong()))
            }while (cursor.moveToNext())
        }
        startActivity<MoneyPalActivity>()
    }
}
