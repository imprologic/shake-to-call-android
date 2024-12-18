package com.imprologic.shaketocall.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlin.math.sqrt


class MonitoringService : Service(), SensorEventListener {

    val tag = "MonitoringService"
    private var shakeThreshold = 12.0f

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var shakeHandler: Handler? = Handler()
    private var isShaking = false

    override fun onCreate() {
        super.onCreate()
        Log.i(tag, "onCreate")
        createNotificationChannel()
        startForeground(1, createNotification())
        Log.d("ShakeService", "Service started")
        // Initialize shake detection and telephony handling here
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(tag, "onStartCommand")
        // Handle any intent data if needed
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ShakeService", "Service destroyed")
        // Clean up resources
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(tag, "onBind")
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(tag, "createNotificationChannel")
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
        Log.i(tag, "createNotification")
        return NotificationCompat.Builder(this, "shake_service_channel")
            .setContentTitle("Shake Detection Running")
            .setContentText("Monitoring for shake events and calls")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
    }


    // SensorEventListener implementation

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val shakeMagnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            if (shakeMagnitude > shakeThreshold) {
                if (!isShaking) {
                    Log.i(tag, "Shake detected")
                    isShaking = true
                }
                shakeHandler?.removeCallbacksAndMessages(null)
            } else if (isShaking) {
                shakeHandler?.postDelayed({
                    Log.i(tag, "Shake ended, calling actOnShake()")
                    actOnShake()
                    isShaking = false
                }, 500)
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        TODO("Not yet implemented")
    }


    // Act on shake

    private fun actOnShake() {
        // TODO: Check if a call is already in progress
        makeCall()
    }


    // Make calls

    private fun makeCall() {
        val phoneToCall = SettingsManager(this).defaultPhone
        Log.i(tag, "Will call $phoneToCall")
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneToCall")
        }
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(callIntent)
    }

}
