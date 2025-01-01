package com.imprologic.shaketocall.services

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class SettingsManager(context: Context) {

    companion object {
        const val DEFAULT_SHAKE_MAGNITUDE = 12.0f
        const val DEFAULT_Z_AXIS_FACTOR = 0.75f
    }


    val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", MODE_PRIVATE)

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

    var shakeToHangUp: Boolean
        get() = prefs.getBoolean("shake_to_hang_up", false)
        set(value) {
            prefs.edit().putBoolean("shake_to_hang_up", value).apply()
        }

    var shakeMagnitude: Float
        get() = prefs.getFloat("shake_magnitude", DEFAULT_SHAKE_MAGNITUDE)
        set(value) {
            prefs.edit().putFloat("shake_magnitude", value).apply()
        }

    var zAxisFactor: Float
        get() = prefs.getFloat("z_axis_factor", DEFAULT_Z_AXIS_FACTOR)
        set(value) {
            prefs.edit().putFloat("z_axis_factor", value).apply()
        }


    fun resetAdvancedPreferences() {
        shakeMagnitude = DEFAULT_SHAKE_MAGNITUDE
        zAxisFactor = DEFAULT_Z_AXIS_FACTOR
    }

}