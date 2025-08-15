package com.imprologic.shaketocall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.services.MonitoringServiceStarter
import com.imprologic.shaketocall.services.PermissionHelper
import com.imprologic.shaketocall.ui.AdvancedOptions
import com.imprologic.shaketocall.ui.AnswerOptions
import com.imprologic.shaketocall.ui.CallOptions
import com.imprologic.shaketocall.ui.ExtraOptions
import com.imprologic.shaketocall.ui.theme.MainTheme

class MainActivity : ComponentActivity() {

    private lateinit var permissionHelper: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
        permissionHelper = PermissionHelper(this)
        permissionHelper.registerPermissionHandler()
        MonitoringServiceStarter.manageService(this)
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(
) {
    MainTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                    ),
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                CallOptions()
                AnswerOptions()
                AdvancedOptions()
                ExtraOptions()
            }
        }
    }
}




