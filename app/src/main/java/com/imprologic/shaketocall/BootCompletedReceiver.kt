package com.imprologic.shaketocall

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.utils.NotificationUtils

class BootCompletedReceiver : BroadcastReceiver() {

    private val tag = "BootCompletedReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            Log.i(tag, "Received action ${intent?.action}, ignoring.")
            return
        }
        if (context == null) {
            Log.w(tag, "Received null context, cannot proceed.")
            return
        }

        // On Android 15+, we cannot start the foreground service directly.
        // We must show a notification to the user to start the app.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15
            Log.i(tag, "Device rebooted on Android 15 or newer.")
            val settingsManager = SettingsManager(context)
            if (settingsManager.anyShakeOptionEnabled) {
                Log.i(tag, "Showing notification to user.")
                NotificationUtils.showRestartNotification(context)
            }
        } else {
            Log.i(tag, "Device rebooted. Running manageService...")
            try {
                // Keep the old behavior for older Android versions
                MonitoringServiceStarter.manageService(context)
            } catch (e: Exception) {
                Log.e(tag, "Exception while running manageService", e)
            }
        }
    }
}
