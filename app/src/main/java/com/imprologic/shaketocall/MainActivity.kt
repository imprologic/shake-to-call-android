package com.imprologic.shaketocall

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.imprologic.shaketocall.ui.theme.MainTheme
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {

    private var tag = "MainActivity"
    private var requiredPermissions = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        arrayOf(Manifest.permission.CALL_PHONE, Manifest.permission.ANSWER_PHONE_CALLS)
        else arrayOf(Manifest.permission.CALL_PHONE)
    private var shakeThreshold = 12.0f

    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var sensorManager: SensorManager

    private var accelerometer: Sensor? = null
    private var isRinging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showLayout()
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
        enableEdgeToEdge()
        setContent {
            MainTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
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
                    // answerCall()
                } else {
                    // makeCall()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not required for this use case
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