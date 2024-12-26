package com.imprologic.shaketocall.services

import android.util.Log


class ShakeStateMachine(
    private val onShakeConfirmed: () -> Unit
) {
    val tag = "ShakeStateMachine"

    val confirmInterval = 0.5 * 1000;
    val ignoreInterval = 5 * 1000

    var lastInitiationTime = 0L
    var waitingForConfirmation = false;


    fun handleShakeEvent() {
        val now = System.currentTimeMillis()
        val delta = now - lastInitiationTime;
        if (delta > ignoreInterval) {
            Log.i(tag, "Shake initiated, waiting for confirmation")
            lastInitiationTime = now
            waitingForConfirmation = true
            return
        }
        if (delta < confirmInterval) {
            return
        }
        if (waitingForConfirmation) {
            Log.i(tag, "Shake confirmed")
            waitingForConfirmation = false
            onShakeConfirmed()
        }
    }



}