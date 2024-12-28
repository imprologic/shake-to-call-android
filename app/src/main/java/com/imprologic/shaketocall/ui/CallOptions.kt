package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.SettingsManager
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
            title = stringResource(id = R.string.shake_to_call),
            subtitle = stringResource(id = R.string.shake_to_call_description),
            value = settingsManager.shakeToCall,
            onValueChange = {
                settingsManager.shakeToCall = it
            }
        )
    }
}