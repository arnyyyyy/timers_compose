package com.arno.timers_compose.feature_auth.view

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.R
import com.arno.timers_compose.core.AppViewModelProvider
import com.arno.timers_compose.feature_auth.AuthViewModel

@Composable
fun AuthScreen(
        authViewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
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
                                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
                ProgressNotificationAlertDialog(
                        onDismissRequest = {
                                shouldShowPromotedNotificationsDialog = false
                                onAuthSuccess()
                        },
                        onAuthSuccess = {
                                shouldShowPromotedNotificationsDialog = false
                                onAuthSuccess()
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
                                        AuthErrorCard(error)
                                }
                        }
                }
        }
}
