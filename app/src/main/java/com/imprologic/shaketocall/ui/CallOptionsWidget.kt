package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.SettingsManager


@Composable
fun CallOptionsWidget(
    settingsManager: SettingsManager,
    modifier: Modifier
) {

    val textState = remember { mutableStateOf(settingsManager.defaultPhone ?: "") }

    OutlinedCard(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.shake_to_call),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
            AccessibleSwitch(
                initialChecked = settingsManager.shakeToCall,
                onCheckedChange = {
                    settingsManager.shakeToCall = it
                }
            )
            TextField(
                value = textState.value,
                onValueChange = {
                    textState.value = it
                    settingsManager.defaultPhone = it
                },
                label = { Text(text = "Enter phone to call") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}