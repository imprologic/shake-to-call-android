package com.imprologic.shaketocall.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.imprologic.shaketocall.MainActivity
import com.imprologic.shaketocall.R

object NotificationUtils {
    private const val CHANNEL_ID = "SHAKE_TO_CALL_SERVICE_FAILURE"

    fun showRestartNotification(context: Context) {
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
            .setSmallIcon(R.drawable.ic_stat_warning)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
