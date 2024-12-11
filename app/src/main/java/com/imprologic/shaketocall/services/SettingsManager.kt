package com.imprologic.shaketocall.services

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.activity.ComponentActivity


class SettingsManager(activity: ComponentActivity) {

    val prefs: SharedPreferences = activity.getSharedPreferences("app_prefs", MODE_PRIVATE)

    var shakeToCall: Boolean
        get() = prefs.getBoolean("shake_to_call", false)
        set(value) {
            prefs.edit().putBoolean("shake_to_call", value).apply()
        }

    var defaultPhone: String?
        get() = prefs.getString("phone_number", null)
        set(value) {
            prefs.edit().putString("phone_number", value).apply()
        }

    var shakeToAnswer: Boolean
        get() = prefs.getBoolean("shake_to_answer", false)
        set(value) {
            prefs.edit().putBoolean("shake_to_answer", value).apply()
        }

}