package com.imprologic.shaketocall.services

import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log
import androidx.core.content.ContextCompat


class PermissionHelper(private val activity: ComponentActivity) {

    val tag = "PermissionHelper"

    private val requiredPermissions: List<String>
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                return listOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ANSWER_PHONE_CALLS,
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                return listOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.ANSWER_PHONE_CALLS,
                )
            return listOf(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            )
        }


    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>


    fun registerPermissionHandler() {
        // Initialize the launcher for requesting permissions
        requestPermissionsLauncher = activity.registerForActivityResult(
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
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
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

}