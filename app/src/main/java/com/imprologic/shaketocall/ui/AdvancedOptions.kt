package com.imprologic.shaketocall.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.ui.widgets.PreferenceSection
import com.imprologic.shaketocall.ui.widgets.SliderPreference


@Composable
fun AdvancedOptions() {
    val context = LocalContext.current
    val settingsManager = SettingsManager(context)
    val shakeMagnitudeState = remember { mutableFloatStateOf(settingsManager.shakeMagnitude) }

    PreferenceSection(
        title = stringResource(R.string.advanced_options),
    ) {
        SliderPreference(
            title = stringResource(R.string.shake_magnitude),
            metric = stringResource(R.string.shake_metric),
            value = shakeMagnitudeState.floatValue,
            onValueChange = {
                shakeMagnitudeState.floatValue = it
                settingsManager.shakeMagnitude = it
                MonitoringServiceStarter.restartService(context)
            },
            valueRange = 10f.rangeTo(20f)
        )
    }

}