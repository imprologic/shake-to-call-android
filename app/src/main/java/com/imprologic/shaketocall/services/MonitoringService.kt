package com.imprologic.shaketocall.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.imprologic.shaketocall.MainActivity
import com.imprologic.shaketocall.R
import kotlin.math.sqrt

private const val channelName = "shake_service_channel"

class MonitoringService : Service(), SensorEventListener {

    val tag = "MonitoringService"

    private var shakeThreshold = 12.0f

    private lateinit var sensorManager: SensorManager
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var settingsManager: SettingsManager
    private var accelerometer: Sensor? = null
    private val shakeStateMachine = ShakeStateMachine(::actOnShake)

    private var phoneState = 0


    override fun onCreate() {
        super.onCreate()
        Log.i(tag, "onCreate")
        createMainNotificationChannel()
        startForeground(1, createNotification())
        Log.d(tag, "Service started")
        settingsManager = SettingsManager(this)
        shakeThreshold = settingsManager.shakeMagnitude
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
        Log.d(tag, "Service destroyed")
        sensorManager.unregisterListener(this)
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        stopForeground(true)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(tag, "onBind")
        return null
    }

    private fun createMainNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(tag, "createNotificationChannel")
            val channel = NotificationChannel(
                channelName,
                "Shake Detection Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }


    private fun createNotification(): Notification {
        Log.i(tag, "createNotification")
        // Create an Intent to open MainActivity
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Create a PendingIntent for the Intent
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Create the notification
        return NotificationCompat.Builder(this, channelName)
            .setContentTitle(this.getString(R.string.service_notification_title))
            .setContentText(this.getString(R.string.service_notification_content))
            .setSmallIcon(R.drawable.ic_stat_monitoring_service)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
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
                shakeStateMachine.handleShakeEvent()
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed
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
        if (!settingsManager.shakeToCall) {
            Log.i(tag, "shakeToCall option is disabled")
            return
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(tag, "CALL_PHONE permission denied")
            return
        }
        val phoneToCall = settingsManager.defaultPhone
        Log.i(tag, "Will call $phoneToCall")
        notifyShakeConfirmed()
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val uri = Uri.fromParts("tel", phoneToCall, null)
        val bundle = Bundle()
        bundle.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, true)
        telecomManager.placeCall(uri, bundle)
    }


    // End call

    private fun endCall() {
        if (!settingsManager.shakeToHangUp) {
            // TODO: Should we have a different setting here?
            Log.i(tag, "shakeToHangUp option is disabled")
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            Log.e(tag, "Call ending not supported for version ${Build.VERSION.SDK_INT}")
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
        notifyShakeConfirmed()
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        if (telecomManager.isInCall) {
            telecomManager.endCall()
        }
    }

    // Answer call

    private fun answerCall() {
        if (!settingsManager.shakeToAnswer) {
            Log.i(tag, "shakeToAnswer option is disabled")
            return
        }
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
        notifyShakeConfirmed()
        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        if (telecomManager.isInCall) {
            telecomManager.acceptRingingCall()
        }
    }


    private fun notifyShakeConfirmed() {
        try {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(effect)
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        } catch (e: Error) {
           Log.w(tag, "Unable to vibrate", e)
        }
    }

}
