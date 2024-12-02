package com.imprologic.shaketocall

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.imprologic.shaketocall.ui.AnswerOptionsWidget
import com.imprologic.shaketocall.ui.CallOptionsWidget

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldContainer()
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldContainer() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        },
    ) { innerPadding ->
        AdaptiveLayout(innerPadding)
    }
}


@Composable
fun AdaptiveLayout(innerPadding: PaddingValues) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CallOptionsWidget(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .fillMaxHeight()
            )
            AnswerOptionsWidget(
                modifier = Modifier
                    .padding(PaddingValues(0.dp, 16.dp, 16.dp, 16.dp))
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CallOptionsWidget(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1.1f)
                    .fillMaxWidth()
            )
            AnswerOptionsWidget(
                modifier = Modifier
                    .padding(PaddingValues(16.dp, 0.dp, 16.dp, 16.dp))
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}



