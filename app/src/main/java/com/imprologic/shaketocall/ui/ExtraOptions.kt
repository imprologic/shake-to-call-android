package com.imprologic.shaketocall.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.imprologic.shaketocall.R
import com.imprologic.shaketocall.ui.widgets.ClickablePreference
import com.imprologic.shaketocall.ui.widgets.PreferenceSection
import androidx.core.net.toUri


@Composable
fun ExtraOptions() {
    val context = LocalContext.current

    PreferenceSection(
        title = stringResource(R.string.extras_section),
    ) {
        ClickablePreference(
            title = stringResource(R.string.other_apps_title),
            subtitle = stringResource(R.string.other_apps_description),
            onClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/developer?id=imprologic".toUri()
                    )
                )
            }
        )
    }

}


