package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.ui.widgets.PhonePreference
import com.imprologic.shaketocall.ui.widgets.SwitchPreference


@Composable
fun CallOptions() {

    val context = LocalContext.current
    val settingsManager = SettingsManager(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SwitchPreference(
            title = stringResource(R.string.shake_to_call),
            subtitle = stringResource(R.string.shake_to_call_description),
            value = settingsManager.shakeToCall,
            onValueChange = {
                settingsManager.shakeToCall = it
                MonitoringServiceStarter.manageService(context)
            }
        )
        PhonePreference(
            title = stringResource(R.string.label_number_to_call),
            subtitle = settingsManager.defaultPhone
                ?: stringResource(R.string.description_number_to_call),
            value = settingsManager.defaultPhone,
            onValueChange = {
                settingsManager.defaultPhone = it
            }
        )
    }
}