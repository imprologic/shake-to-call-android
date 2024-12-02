package com.imprologic.shaketocall.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat


class MonitoringService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, createNotification())
        Log.d("ShakeService", "Service started")
        // Initialize shake detection and telephony handling here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Handle any intent data if needed
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShakeService", "Service destroyed")
        // Clean up resources
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "shake_service_channel",
                "Shake Detection Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "shake_service_channel")
            .setContentTitle("Shake Detection Running")
            .setContentText("Monitoring for shake events and calls")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }}
