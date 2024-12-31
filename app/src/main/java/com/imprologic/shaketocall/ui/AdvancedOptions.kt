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
import com.imprologic.shaketocall.ui.widgets.SliderPreference
import com.imprologic.shaketocall.ui.widgets.SwitchPreference


@Composable
fun AdvancedOptions() {
    val context = LocalContext.current
    val settingsManager = SettingsManager(context)

    PreferenceSection(
        title = stringResource(R.string.advanced_options),
    ) {
        SliderPreference(
            title = stringResource(R.string.shake_magnitude),
            metric = stringResource(R.string.shake_metric),
            value = 12f,
            onValueChange = {}
        )
    }

}