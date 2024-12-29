package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.ui.widgets.SwitchPreference


@Composable
fun AnswerOptions() {
    val context = LocalContext.current
    val settingsManager = SettingsManager(context)
    val shakeToAnswerState = remember { mutableStateOf(settingsManager.shakeToAnswer) }

    Column() {
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
    }

}