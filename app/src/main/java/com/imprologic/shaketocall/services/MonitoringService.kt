package com.imprologic.shaketocall.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import kotlin.math.sqrt


class MonitoringService : Service(), SensorEventListener {

    val tag = "MonitoringService"
    val shakeActionDelay = 5000L

    private var shakeThreshold = 12.0f  // TODO: get this from Settings
    private var lastActionTime  = 0L;

    private lateinit var sensorManager: SensorManager
    private lateinit var telephonyManager: TelephonyManager
    private var accelerometer: Sensor? = null

    private var phoneState = 0


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
        //
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
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
                val now = System.currentTimeMillis()
                if (now - lastActionTime > shakeActionDelay) {
                    Log.i(tag, "Shake detected")
                    lastActionTime = now
                    actOnShake()
                }
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        TODO("Not yet implemented")
    }


    // Phone events listener

    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            phoneState = state
            Log.i(tag, "Phone state changed: $phoneState")
        }
    }

    // Act on shake

    private fun actOnShake() {
        Log.i(tag, "Phone state is $phoneState")
        if (phoneState == TelephonyManager.CALL_STATE_IDLE) {
            makeCall()
        } else if (phoneState == TelephonyManager.CALL_STATE_OFFHOOK) {
            endCall()
        } else if (phoneState == TelephonyManager.CALL_STATE_RINGING) {
            answerCall()
        }
    }


    // Make call

    private fun makeCall() {
        val phoneToCall = SettingsManager(this).defaultPhone
        Log.i(tag, "Will call $phoneToCall")
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$phoneToCall")
        }
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(callIntent)
    }


    // End call

    private fun endCall() {
        // TODO: Implement this
    }

    // Answer call

    private fun answerCall() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.e(tag, "Call answering not supported for version ${Build.VERSION.SDK_INT}")
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(tag, "READ_PHONE_STATE permission not granted, cannot answer call")
            return
        }
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        if (telecomManager.isInCall) {
            telecomManager.acceptRingingCall()
        }
    }


}
