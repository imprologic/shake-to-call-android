package com.imprologic.shaketocall

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.CheckBox
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.imprologic.shaketocall.ui.theme.MainTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {

    private var tag = "MainActivity"
    private var requiredPermissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ANSWER_PHONE_CALLS
        )
        else arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
        )
    private var shakeThreshold = 12.0f
    private val predefinedNumber = "5555555555"

    private lateinit var preferences: SharedPreferences
    private lateinit var shakeToAnswerCheckbox: CheckBox

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var sensorManager: SensorManager

    private var accelerometer: Sensor? = null
    private var isRinging = false
    private var telephonyManager: TelephonyManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        showLayout()
        loadPreferences()
        registerPermissionHandler()
        setupSensors()
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    private fun showLayout() {
        setContentView(R.layout.activity_main)
        shakeToAnswerCheckbox = findViewById(R.id.option_shake_to_answer)
        shakeToAnswerCheckbox.setOnCheckedChangeListener {
                buttonView, isChecked -> onPreferencesChange()
        }

    }


    private fun loadPreferences() {
        // Retrieve and set stored preferences
        shakeToAnswerCheckbox.isChecked = preferences.getBoolean("shake_to_answer", false)
    }


    private fun registerPermissionHandler() {
        // Initialize the launcher for requesting permissions
        requestPermissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val grantedPermissions = requiredPermissions.filter {
                permissions[it] == true
            }
            Log.i(tag, "Granted permissions: $grantedPermissions")
        }

        // Check permissions and request if necessary
        checkAndRequestPermissions()
    }


    private fun checkAndRequestPermissions() {
        val permissionsToRequest = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        Log.i(tag, "Permissions needed: $permissionsToRequest")

        if (permissionsToRequest.isNotEmpty()) {
            // Launch the permission request for the missing permissions
            requestPermissionsLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // Permissions are already granted
            Log.i(tag, "Permissions already granted: $requiredPermissions")
        }
    }


    private fun setupSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val z = it.values[2]

            val shakeMagnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            if (shakeMagnitude > shakeThreshold) {
                Log.i(tag, "Shake detected")
                if (isRinging) {
                    Log.i(tag, "Will answer call")
                    answerCall()
                } else {
                    Log.i(tag, "Will make call")
                    makeCall()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not required for this use case
    }


    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            isRinging = (state == TelephonyManager.CALL_STATE_RINGING)
            Log.i(tag, "Phone state changed, isRinging = $isRinging")
        }
    }

    private fun answerCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            if (telecomManager.isInCall) {
                telecomManager.acceptRingingCall()
            }
        }
    }

    private fun makeCall() {
        val callIntent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$predefinedNumber")
        }
        startActivity(callIntent)
    }


    private fun onPreferencesChange() {
        preferences.edit().apply {
            putBoolean("shake_to_answer", shakeToAnswerCheckbox.isChecked)
            apply()
        }
    }

}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainTheme {
        Greeting("Android")
    }
}