package com.imprologic.shaketocall.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.ui.widgets.DialogPreference
import com.imprologic.shaketocall.ui.widgets.PhoneEntryDialog
import com.imprologic.shaketocall.ui.widgets.PhonePicker
import com.imprologic.shaketocall.ui.widgets.PreferenceSection
import com.imprologic.shaketocall.ui.widgets.SwitchPreference


@Composable
fun CallOptions() {

    val context = LocalContext.current
    val settingsManager = SettingsManager(context)
    val shakeToCallState = remember { mutableStateOf(settingsManager.shakeToCall) }
    val defaultPhoneState = remember { mutableStateOf(settingsManager.defaultPhone) }


    PreferenceSection(
        title = stringResource(R.string.incoming_call_options)
    ) {
        SwitchPreference(
            title = stringResource(R.string.shake_to_call),
            subtitle = stringResource(R.string.shake_to_call_description),
            value = shakeToCallState.value,
            onValueChange = {
                shakeToCallState.value = it
                settingsManager.shakeToCall = it
                MonitoringServiceStarter.manageService(context)
            }
        )
        DialogPreference(
            title = stringResource(R.string.label_number_to_call),
            subtitle = defaultPhoneState.value
                ?: stringResource(R.string.description_number_to_call),
            dialogContent = {
                onDismissRequest ->
                PhoneEntryDialog(
                    value = defaultPhoneState.value,
                    onDismiss = { onDismissRequest() },
                    onConfirm = {
                        onDismissRequest()
                        defaultPhoneState.value = it
                    },
                    onCancel = { onDismissRequest() },
                )
            },
            sideContent = {
                PhonePicker(
                    onPhoneNumberPicked = {
                        defaultPhoneState.value = it
                        settingsManager.defaultPhone = it
                    }
                )
            },
        )
    }
}