package com.imprologic.shaketocall.services

import android.content.Context
import android.content.Intent
import android.os.Build

class MonitoringServiceStarter {

    companion object {

        fun startService(context: Context) {
            val intent = Intent(context, MonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

    }
}