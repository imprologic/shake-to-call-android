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
import com.imprologic.shaketocall.ui.widgets.SwitchPreference


@Composable
fun AnswerOptions() {
    val context = LocalContext.current
    val settingsManager = SettingsManager(context)

    Column() {
        SwitchPreference (
            title = stringResource(R.string.shake_to_answer),
            subtitle = stringResource(R.string.shake_to_answer_description),
            value = settingsManager.shakeToAnswer,
            onValueChange = {
                settingsManager.shakeToAnswer = it
                MonitoringServiceStarter.manageService(context)
            }
        )
    }

}