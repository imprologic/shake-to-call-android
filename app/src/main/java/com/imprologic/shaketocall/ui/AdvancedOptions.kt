package com.imprologic.shaketocall.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.SettingsManager
import com.imprologic.shaketocall.ui.widgets.ConfirmationDialog
import com.imprologic.shaketocall.ui.widgets.DialogPreference
import com.imprologic.shaketocall.ui.widgets.PreferenceSection
import com.imprologic.shaketocall.ui.widgets.SliderPreference


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
                shakeMagnitudeState.floatValue = it
                settingsManager.shakeMagnitude = it
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
                zAxisFactorState.floatValue = it
                settingsManager.zAxisFactor = it
                MonitoringServiceStarter.restartService(context)
            },
            valueRange = 0f.rangeTo(1f),
            steps = 19
        )
        DialogPreference(
            title = stringResource(R.string.reset_advanced_preferences),
            subtitle = stringResource(R.string.reset_advanced_preferences_description),
            dialogContent = {
                onDismissRequest ->
                    ConfirmationDialog(
                        dialogTitle = stringResource(R.string.reset_advanced_preferences_confirmation),
                        onDismiss = { onDismissRequest() },
                        onCancel = { onDismissRequest() },
                        onConfirm = {
                            onDismissRequest()
                            settingsManager.resetAdvancedPreferences()
                            shakeMagnitudeState.floatValue = settingsManager.shakeMagnitude
                            zAxisFactorState.floatValue = settingsManager.zAxisFactor
                        }
                    )
            }
        )
    }

}


