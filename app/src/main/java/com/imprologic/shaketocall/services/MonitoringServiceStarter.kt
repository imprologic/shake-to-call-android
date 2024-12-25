package com.imprologic.shaketocall.services

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class MonitoringServiceStarter {

    companion object {

        const val TAG = "MonitoringServiceStarter"

        private fun startService(context: Context) {
            Log.i(TAG, "Monitoring service will start on SDK " + Build.VERSION.SDK_INT)
            val intent = Intent(context, MonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }


        private fun stopService(context: Context) {
            Log.i(TAG, "Stopping monitoring service...")
            val intent = Intent(context, MonitoringService::class.java)
            val success = context.stopService(intent)
            Log.i(TAG, "Stopping monitoring service result: $success")
        }


        private fun isServiceRunning(context: Context): Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (MonitoringService::class.java.name == service.service.className) {
                    return true
                }
            }
            return false
        }


        fun manageService(context: Context) {
            val settingsManager = SettingsManager(context)
            val shouldRun = settingsManager.shakeToCall || settingsManager.shakeToAnswer
            val isRunning = isServiceRunning(context)
            Log.i(TAG, "shouldRun: $shouldRun, isRunning: $isRunning")
            if (shouldRun == isRunning) {
                return
            }
            if (shouldRun) {
                startService(context)
            } else {
                stopService(context)
            }
        }



    }
}