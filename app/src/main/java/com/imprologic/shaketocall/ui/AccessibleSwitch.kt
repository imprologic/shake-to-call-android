package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imprologic.shaketocall.R


@Composable
fun AccessibleSwitch(
    initialChecked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    
    val checkedState = remember { mutableStateOf(initialChecked) }

    var onLabel = stringResource(id = R.string.label_on)
    var offLabel = stringResource(id = R.string.label_off)

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                onCheckedChange(it)
            },
            thumbContent = if (checkedState.value) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
        Text(
            modifier = Modifier
                .padding(8.dp),
            text = if (checkedState.value) onLabel else offLabel,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


