package com.arno.timers_compose.feature_crud.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.R
import com.arno.timers_compose.core.AppViewModelProvider
import com.arno.timers_compose.feature_crud.CreateTimerData
import com.arno.timers_compose.feature_crud.CreateTimerViewModel
import com.arno.timers_compose.feature_store_timers.TimerCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimerScreen(
        onNavigateBack: () -> Unit,
        initialCategory: TimerCategory? = null,
        viewModel: CreateTimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        var timerData by remember {
                mutableStateOf(
                        CreateTimerData(
                                category = initialCategory ?: TimerCategory.OTHER
                        )
                )
        }

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = {
                                        Text(
                                                text = "Новый таймер",
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                )
                                        )
                                },
                                navigationIcon = {
                                        IconButton(onClick = onNavigateBack) {
                                                Icon(
                                                        Icons.AutoMirrored.Filled.ArrowBack,
                                                        contentDescription = stringResource(R.string.back)
                                                )
                                        }
                                },
                                actions = {
                                        IconButton(
                                                onClick = {
                                                        viewModel.addTimer(timerData)
                                                        onNavigateBack()
                                                }
                                        ) {
                                                Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = stringResource(R.string.create_timer)
                                                )
                                        }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                        )
                },
                containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
                TimerFormContent(
                        timerData = timerData,
                        onTimerDataChange = { timerData = it },
                        modifier = Modifier.padding(innerPadding)
                )
        }
}