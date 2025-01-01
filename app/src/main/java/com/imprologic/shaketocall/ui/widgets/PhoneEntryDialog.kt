package com.imprologic.shaketocall.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import com.imprologic.shaketocall.R


@Composable
fun PhoneEntryDialog(
    value: String?,
    onDismiss: () -> Unit,
    onConfirm: (result: String?) -> Unit,
    onCancel: () -> Unit,
) {
    val resultState = remember { mutableStateOf(value) }

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.alert_number_to_call),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
                TextField(
                    value = resultState.value ?: "",
                    onValueChange = {
                        resultState.value = it
                    },
                    keyboardOptions = KeyboardOptions(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onCancel() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(android.R.string.cancel))
                    }
                    TextButton(
                        onClick = { onConfirm( resultState.value ) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(android.R.string.ok))
                    }
                }
            }
        }
    }
}