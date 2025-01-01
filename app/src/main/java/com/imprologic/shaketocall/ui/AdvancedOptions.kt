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
import kotlin.math.round


@Composable
fun AdvancedOptions() {
    val context = LocalContext.current
    val settingsManager = SettingsManager(context)
    val shakeMagnitudeState = remember { mutableFloatStateOf(settingsManager.shakeMagnitude) }
    val zAxisFactorState = remember { mutableFloatStateOf(settingsManager.zAxisFactor) }

    PreferenceSection(
        title = stringResource(R.string.advanced_options),
    ) {
        SliderPreference(
            title = stringResource(R.string.shake_magnitude),
            metric = stringResource(R.string.shake_metric),
            value = shakeMagnitudeState.floatValue,
            onValueChange = {
                val newValue = round(100 * it) / 100
                shakeMagnitudeState.floatValue = newValue
                settingsManager.shakeMagnitude = newValue
                MonitoringServiceStarter.restartService(context)
            },
            valueRange = 10f.rangeTo(20f),
            steps = 19
        )
        SliderPreference(
            title = stringResource(R.string.z_axis_factor),
            metric = "",
            value = zAxisFactorState.floatValue,
            onValueChange = {
                val newValue = round(100 * it) / 100
                zAxisFactorState.floatValue = newValue
                settingsManager.zAxisFactor = newValue
                MonitoringServiceStarter.restartService(context)
            },
            valueRange = 0f.rangeTo(1f),
            steps = 19
        )
    }

}