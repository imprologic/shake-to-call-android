package com.imprologic.shaketocall.ui.widgets

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


@Composable
fun PhonePicker(
    onPhoneNumberPicked: (String) -> Unit
) {
    val context = LocalContext.current
    val permissionState = remember { mutableStateOf(false) }

    // Check initial permission state
    LaunchedEffect(Unit) {
        permissionState.value = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request permission if not granted
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionState.value = isGranted
        // TODO: Launch the system picker if the permission is granted
    }

    // Contact picker launcher
    val contactPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Uri? = result.data?.data
            data?.let {
                context.contentResolver.query(
                    it,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    null,
                    null,
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val phoneNumber = cursor.getString(numberIndex)
                        if (phoneNumber != null) {
                            onPhoneNumberPicked(phoneNumber)
                        }
                    }
                }
            }
        }
    }

    IconButton(
        onClick = {
            if (!permissionState.value) {
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            } else {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
                contactPickerLauncher.launch(intent)
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
    }

}
