package com.imprologic.shaketocall.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat

class PermissionChecker {

    companion object {

        const val tag = "PermissionChecker"


        fun canAnswerCall(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Log.e(tag, "Call answering not supported for version ${Build.VERSION.SDK_INT}")
                return false
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(tag, "READ_PHONE_STATE permission not granted, cannot answer call")
                return false
            }
            return true
        }


        fun canEndCall(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                Log.e(tag, "Call ending not supported for version ${Build.VERSION.SDK_INT}")
                return false
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e(tag, "READ_PHONE_STATE permission not granted, cannot answer call")
                return false
            }
            return true
        }

    }
}