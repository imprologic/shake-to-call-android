package com.imprologic.shaketocall.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DialogEntryPreference(
    title: String,
    subtitle: String,
    dialogTitle: String,
    value: String?,
    onValueChange: (String?) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    sideContent: (@Composable () -> Unit)?
) {
    val dialogState = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClick = { dialogState.value = !dialogState.value }
                )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = value ?: subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        when {
            sideContent != null ->
                sideContent()
        }
        when {
            dialogState.value ->
                PhoneEntryDialog(
                    title = dialogTitle,
                    value = value,
                    keyboardOptions = keyboardOptions,
                    onDismissRequest = {
                        dialogState.value = false
                    },
                    onConfirmation = {
                        dialogState.value = false
                        onValueChange(it)
                    }
                )
        }
    }

}

