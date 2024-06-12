package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.mikicorp.step.R

@Suppress("DEPRECATION")
class SimUtil (
    val onSMSSend: (result: Boolean) -> Unit,
    val onSMSDelivered: (result: Boolean) -> Unit
) {
    @SuppressLint("MissingPermission", "UnspecifiedRegisterReceiverFlag")
    fun sendSMS(context: Context, simSelected: Int, phoneNumber: String, message: String) {
        val sent = "SMS_SENT"
        val delivered = "SMS_DELIVERED"

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

        val sentPI: PendingIntent = PendingIntent.getBroadcast(
            context, 0, Intent(
                sent
            ), flags
        )

        val deliveredPI = PendingIntent.getBroadcast(
            context, 0,
            Intent(delivered), flags
        )
        // ---when the SMS has been sent---

        val sendSMS: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                onSMSSend(resultCode == Activity.RESULT_OK)
            }
        }

        // DELIVERY BroadcastReceiver
        val deliverSMS: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(arg0: Context?, arg1: Intent?) {
                onSMSDelivered(resultCode == AppCompatActivity.RESULT_OK)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(sendSMS, IntentFilter(sent), Context.RECEIVER_EXPORTED)
            context.registerReceiver(deliverSMS, IntentFilter(delivered), Context.RECEIVER_EXPORTED)
        }else {
            context.registerReceiver(sendSMS, IntentFilter(sent))
            context.registerReceiver(deliverSMS, IntentFilter(delivered))
        }
        val localSubscriptionManager = getSystemService(context, SubscriptionManager::class.java) as SubscriptionManager
        val localList: List<*> = localSubscriptionManager.activeSubscriptionInfoList
        val simInfo = localList[simSelected] as SubscriptionInfo
//        val smsManager: SmsManager = SmsManager.getDefault()
//        val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

        val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            getSystemService(context, SmsManager::class.java)!!.createForSubscriptionId(simInfo.subscriptionId)
        } else {
            SmsManager.getSmsManagerForSubscriptionId(simInfo.subscriptionId)
        }
        try {
            smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)
        } catch (e: Exception) {
            Toast.makeText(context, context.resources.getString(R.string.send_sms_error), Toast.LENGTH_SHORT).show()
        }

    }
}
