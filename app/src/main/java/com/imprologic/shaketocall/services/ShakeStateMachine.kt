package com.imprologic.shaketocall.services

import android.util.Log


class ShakeStateMachine(
    private val onShakeConfirmed: () -> Unit
) {
    val tag = "ShakeStateMachine"

    val confirmInterval = 0.5 * 1000;
    val ignoreInterval = 5 * 1000

    var lastEventTime = 0L


    fun handleShakeEvent() {
        val now = System.currentTimeMillis()
        val delta = now - lastEventTime;
        if (delta > ignoreInterval) {
            Log.i(tag, "Shake initiated, waiting for confirmation")
            lastEventTime = now
            return
        }
        if (delta < confirmInterval) {
            return
        }
        Log.i(tag, "Shake confirmed")
        lastEventTime = now
        onShakeConfirmed()
    }



}