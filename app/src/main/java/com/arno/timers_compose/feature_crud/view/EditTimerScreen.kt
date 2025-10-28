package com.arno.timers_compose.feature_crud.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.core.AppViewModelProvider
import com.arno.timers_compose.feature_crud.CreateTimerData
import com.arno.timers_compose.feature_crud.CreateTimerViewModel
import com.arno.timers_compose.feature_timers_list.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTimerScreen(
        timerId: String,
        onNavigateBack: () -> Unit,
        timerViewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory),
        createTimerViewModel: CreateTimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        val timersState = timerViewModel.timers.collectAsState()
        val timer = timersState.value.find { it.id == timerId }

        LaunchedEffect(Unit) {
                timerViewModel.refreshTimers()
        }

        if (timer == null) {
                Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                ) {
                        CircularProgressIndicator()
                }
                return
        }

        var timerData by remember {
                mutableStateOf(
                        CreateTimerData(
                                name = timer.name,
                                hours = timer.hours,
                                minutes = timer.minutes,
                                timerType = timer.timerType,
                                selectedDays = timer.selectedDays
                        )
                )
        }

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = {
                                        Text(
                                                text = "Редактирование",
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                )
                                        )
                                },
                                navigationIcon = {
                                        IconButton(onClick = onNavigateBack) {
                                                Icon(
                                                        Icons.AutoMirrored.Filled.ArrowBack,
                                                        contentDescription = "Назад"
                                                )
                                        }
                                },
                                actions = {
                                        IconButton(
                                                onClick = {
                                                        createTimerViewModel.updateTimer(
                                                                timerData,
                                                                timer
                                                        )
                                                        onNavigateBack()
                                                }
                                        ) {
                                                Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Сохранить"
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
