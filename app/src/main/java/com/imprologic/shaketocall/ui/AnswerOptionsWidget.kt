package com.imprologic.shaketocall.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun AnswerOptionsWidget(modifier: Modifier) {
    OutlinedCard(
        modifier = modifier
            .padding(16.dp)
    ) {
        Text(
            text = "Shake to Answer",
            modifier = Modifier
                .padding(16.dp),
        )
    }
}