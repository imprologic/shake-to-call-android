package com.imprologic.shaketocall

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager

class BootCompletedReceiver : BroadcastReceiver() {

    private val tag = "BootCompletedReceiver"
    private val CHANNEL_ID = "SHAKE_TO_CALL_BOOT"

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
                showRestartNotification(context)
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

    private fun showRestartNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Shake To Call Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create an Intent that will open your main activity
        val activityIntent = Intent(context, MainActivity::class.java).apply {
            // You can add extras to inform your activity why it's being opened
            putExtra("from_boot", true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Re-enable Shake To Call")
            .setContentText("Tap here to restart the shake detection service.")
            .setSmallIcon(R.drawable.ic_stat_warning) // Replace with your notification icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
