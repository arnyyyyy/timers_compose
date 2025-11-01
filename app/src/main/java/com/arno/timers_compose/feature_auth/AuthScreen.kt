package com.arno.timers_compose.feature_auth

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.R

@Composable
fun AuthScreen(
        authViewModel: AuthViewModel = viewModel(),
        onAuthSuccess: () -> Unit
) {
        val context = LocalContext.current
        val authState by authViewModel.authState.collectAsState()

        var shouldRequestNotificationPermission by remember { mutableStateOf(false) }
        var shouldShowPromotedNotificationsDialog by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
                authViewModel.initGoogleSignIn(context)
        }

        val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        shouldShowPromotedNotificationsDialog = true
                } else {
                        onAuthSuccess()
                }
        }

        LaunchedEffect(authState.user) {
                if (authState.user != null && !shouldRequestNotificationPermission) {
                        shouldRequestNotificationPermission = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                                onAuthSuccess()
                        }
                }
        }

        val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
                if (result.data != null) {
                        authViewModel.handleSignInResult(result.data)
                } else {
                        if (result.resultCode == Activity.RESULT_OK) {
                                authViewModel.handleSignInResult(null)
                        }
                }
        }

        if (shouldShowPromotedNotificationsDialog) {
                AlertDialog(
                        onDismissRequest = {
                                shouldShowPromotedNotificationsDialog = false
                                onAuthSuccess()
                        },
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
                                                shouldShowPromotedNotificationsDialog = false
                                                onAuthSuccess()
                                        }
                                ) {
                                        Text("Allow")
                                }
                        },
                        dismissButton = {
                                TextButton(
                                        onClick = {
                                                shouldShowPromotedNotificationsDialog = false
                                                onAuthSuccess()
                                        }
                                ) {
                                        Text("Skip")
                                }
                        }
                )
        }

        Scaffold { paddingValues ->
                Box(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(24.dp),
                        contentAlignment = Alignment.Center
                ) {
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                                Text(
                                        text = stringResource(R.string.hello),
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                        text = stringResource(R.string.register),
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                Button(
                                        onClick = {
                                                val signInIntent = authViewModel.getSignInIntent()
                                                if (signInIntent != null) {
                                                        launcher.launch(signInIntent)
                                                }
                                        },
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp),
                                        enabled = !authState.isLoading,
                                        colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                ) {
                                        if (authState.isLoading) {
                                                CircularProgressIndicator(
                                                        modifier = Modifier.size(24.dp),
                                                        color = MaterialTheme.colorScheme.onPrimary
                                                )
                                        } else {
                                                Row(
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                                12.dp
                                                        ),
                                                        verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                        Text(
                                                                text = stringResource(R.string.google_sign_in),
                                                                fontSize = 16.sp,
                                                                fontWeight = FontWeight.Medium
                                                        )
                                                }
                                        }
                                }

                                authState.error?.let { error ->
                                        Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                                )
                                        ) {
                                                Row(
                                                        modifier = Modifier.padding(16.dp),
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                                12.dp
                                                        )
                                                ) {
                                                        Icon(
                                                                imageVector = Icons.Default.Warning,
                                                                contentDescription = null,
                                                                tint = MaterialTheme.colorScheme.error
                                                        )
                                                        Text(
                                                                text = error,
                                                                color = MaterialTheme.colorScheme.onErrorContainer,
                                                                fontSize = 14.sp
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}
