package com.imprologic.shaketocall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.imprologic.shaketocall.services.MonitoringServiceStarter

class BootCompletedReceiver : BroadcastReceiver() {

    val tag = "BootCompletedReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            Log.i(tag, "Received action ${intent?.action}, ignoring.")
            return
        }
        if (context == null) {
            Log.w(tag, "Received null context, cannot start monitoring service!")
            return
        }
        Log.i(tag, "Device rebooted. Running manageService...")
        MonitoringServiceStarter.manageService(context)
    }

}
