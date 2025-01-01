package com.imprologic.shaketocall.ui.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardOptions.Companion
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@Composable
fun DialogPreference(
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
                GenericDialog(
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


@Composable
fun GenericDialog(
    title: String,
    value: String?,
    keyboardOptions: KeyboardOptions,
    onDismissRequest: () -> Unit,
    onConfirmation: (result: String?) -> Unit,
) {
    val resultState = remember { mutableStateOf(value) }

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextField(
                    value = resultState.value ?: "",
                    onValueChange = {
                        resultState.value = it
                    },
                    keyboardOptions = keyboardOptions,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(android.R.string.cancel))
                    }
                    TextButton(
                        onClick = { onConfirmation( resultState.value ) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(android.R.string.ok))
                    }
                }
            }
        }
    }
}