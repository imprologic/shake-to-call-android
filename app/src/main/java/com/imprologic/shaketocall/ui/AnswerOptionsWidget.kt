package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.services.MonitoringServiceStarter


@Composable
fun AnswerOptionsWidget(modifier: Modifier) {
    val context = LocalContext.current

    OutlinedCard(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.shake_to_answer),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp),
            )
            AccessibleSwitch()
            Button(
                onClick = {
                    MonitoringServiceStarter.startService(context)
                }
            ) {
                Text(text = "launch service")
            }
        }
    }
}