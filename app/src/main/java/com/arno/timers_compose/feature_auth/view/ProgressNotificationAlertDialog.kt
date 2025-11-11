package com.arno.timers_compose.feature_auth.view

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ProgressNotificationAlertDialog(
        onDismissRequest: () -> Unit,
        onAuthSuccess: () -> Unit
) {
        val context = LocalContext.current

        AlertDialog(
                onDismissRequest = { onDismissRequest() },
                title = { Text("Enable Live Updates") },
                text = {
                        Text("Enable promoted notifications to see live timer updates in your notification bar.")
                },
                confirmButton = {
                        Button(
                                onClick = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                                val intent =
                                                        Intent(Settings.ACTION_APP_NOTIFICATION_PROMOTION_SETTINGS).apply {
                                                                putExtra(
                                                                        Settings.EXTRA_APP_PACKAGE,
                                                                        context.packageName
                                                                )
                                                        }
                                                context.startActivity(intent)
                                        }
                                        onAuthSuccess()
                                }
                        ) {
                                Text("Allow")
                        }
                },
                dismissButton = {
                        TextButton(
                                onClick = {
                                        onAuthSuccess()
                                }
                        ) {
                                Text("Skip")
                        }
                }
        )
}