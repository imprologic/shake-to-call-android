package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.ui.widgets.PreferenceSection
import com.imprologic.shaketocall.ui.widgets.SwitchPreference


@Composable
fun AnswerOptions() {
    val context = LocalContext.current
    val settingsManager = SettingsManager(context)
    val shakeToAnswerState = remember { mutableStateOf(settingsManager.shakeToAnswer) }
    val shakeToHangUpState = remember { mutableStateOf(settingsManager.shakeToHangUp) }

    PreferenceSection(
        title = stringResource(R.string.incoming_call_options),
    ) {
        SwitchPreference (
            title = stringResource(R.string.shake_to_answer),
            subtitle = stringResource(R.string.shake_to_answer_description),
            value = shakeToAnswerState.value,
            onValueChange = {
                shakeToAnswerState.value = it
                settingsManager.shakeToAnswer = it
                MonitoringServiceStarter.manageService(context)
            }
        )
        SwitchPreference (
            title = stringResource(R.string.shake_to_hang_up),
            subtitle = stringResource(R.string.shake_to_hang_up_description),
            value = shakeToHangUpState.value,
            onValueChange = {
                shakeToHangUpState.value = it
                settingsManager.shakeToHangUp = it
                MonitoringServiceStarter.manageService(context)
            }
        )
    }

}