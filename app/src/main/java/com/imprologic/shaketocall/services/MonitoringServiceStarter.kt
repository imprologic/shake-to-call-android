package com.imprologic.shaketocall.services

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class MonitoringServiceStarter {

    companion object {

        const val TAG = "MonitoringServiceStarter"

        fun startService(context: Context) {
            Log.i(TAG, "Monitoring service will start on SDK " + Build.VERSION.SDK_INT)
            val intent = Intent(context, MonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }
}