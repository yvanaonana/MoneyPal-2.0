package com.example.moneypal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import androidx.core.content.ContextCompat.startActivity
import com.example.moneypal.util.MessagesUtil
import java.util.*

class SMSListener : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        if (extras != null){
//            val sms = extras.get("pdus") as Array<Any>
//
//            for (i in sms.indices){
//                val format = extras.getString("format")
//                var smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                        SmsMessage.createFromPdu(sms[i] as ByteArray, format)
//                    }else{
//                        SmsMessage.createFromPdu(sms[i] as ByteArray)
//                    }
//
//                val phoneNumber = smsMessage.originatingAddress
//                var messageText :String = ""
//                messageText += smsMessage.messageBody.toString()
//
//                if (phoneNumber.equals("OrangeMoney")){
//                    val type = MessagesUtil.determinerTypeMessage(messageText)
//                    MessagesUtil.traiterMessage(messageText, type, Calendar.getInstance().time)
//                }
//            }
            try {
                startActivity(context, Intent(context, SplashActivity::class.java), null)
            }catch (e:Exception){

            }

        }
    }

}