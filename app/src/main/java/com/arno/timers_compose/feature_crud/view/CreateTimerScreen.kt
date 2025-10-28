package com.arno.timers_compose.feature_crud.view

import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.R
import com.arno.timers_compose.core.AppViewModelProvider
import com.arno.timers_compose.feature_crud.CreateTimerData
import com.arno.timers_compose.feature_crud.CreateTimerViewModel
import com.arno.timers_compose.feature_crud.TimerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimerScreen(
        onNavigateBack: () -> Unit,
        viewModel: CreateTimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        var timerData by remember { mutableStateOf(CreateTimerData()) }
        val haptic = LocalHapticFeedback.current

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
                Column(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                        // Название таймера
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                                Column(
                                        modifier = Modifier.padding(20.dp)
                                ) {
                                        Text(
                                                text = "Название",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontWeight = FontWeight.Medium
                                                ),
                                                modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        OutlinedTextField(
                                                value = timerData.name,
                                                onValueChange = { timerData = timerData.copy(name = it) },
                                                modifier = Modifier.fillMaxWidth(),
                                                placeholder = { Text("Мой таймер") },
                                                singleLine = true,
                                                shape = RoundedCornerShape(12.dp),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                                )
                                        )
                                }
                        }

                        // TimePicker
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                                Column(
                                        modifier = Modifier.padding(20.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Text(
                                                text = "Длительность",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontWeight = FontWeight.Medium
                                                ),
                                                modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 16.dp)
                                        )
                                        AndroidView(
                                                factory = { context ->
                                                        val inflater = android.view.LayoutInflater.from(context)
                                                        val timePicker = inflater.inflate(
                                                                R.layout.time_picker_spinner,
                                                                null,
                                                                false
                                                        ) as TimePicker

                                                        timePicker.apply {
                                                                setIs24HourView(true)
                                                                hour = timerData.hours
                                                                minute = timerData.minutes
                                                                setOnTimeChangedListener { _, hourOfDay, minute ->
                                                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                                        timerData = timerData.copy(
                                                                                hours = hourOfDay,
                                                                                minutes = minute
                                                                        )
                                                                }
                                                        }
                                                },
                                                modifier = Modifier
                                                        .padding(bottom = 24.dp)
                                                        .size(250.dp, 250.dp)
                                        )
                                }
                        }

                        // Тип таймера
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                                Column(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp)
                                ) {
                                        Text(
                                                text = "Тип таймера",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontWeight = FontWeight.Medium
                                                ),
                                                modifier = Modifier.padding(bottom = 8.dp)
                                        )

                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                                TimerType.entries.forEach { type ->
                                                        FilterChip(
                                                                selected = timerData.timerType == type,
                                                                onClick = { timerData = timerData.copy(timerType = type) },
                                                                label = {
                                                                        Text(
                                                                                text = when (type) {
                                                                                        TimerType.DAILY -> "Ежедневный"
                                                                                        TimerType.WEEKLY -> "Еженедельный"
                                                                                        TimerType.UNLIMITED -> "Бесконечный"
                                                                                },
                                                                                style = MaterialTheme.typography.labelSmall.copy(
                                                                                        fontWeight = FontWeight.Medium
                                                                                ),
                                                                                maxLines = 1
                                                                        )
                                                                },
                                                                modifier = Modifier.weight(1f),
                                                                colors = FilterChipDefaults.filterChipColors(
                                                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                                )
                                                        )
                                                }
                                        }
                                }
                        }

                        // Дни недели (если выбран DAILY - ежедневный)
                        if (timerData.timerType == TimerType.DAILY) {
                                Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                        Column(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp)
                                        ) {
                                                Text(
                                                        text = "Дни недели",
                                                        style = MaterialTheme.typography.labelLarge.copy(
                                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                                fontWeight = FontWeight.Medium
                                                        ),
                                                        modifier = Modifier.padding(bottom = 8.dp)
                                                )

                                                Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                                ) {
                                                        listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
                                                                val isSelected = timerData.selectedDays.contains(day)
                                                                FilterChip(
                                                                        selected = isSelected,
                                                                        onClick = {
                                                                                timerData = if (isSelected) {
                                                                                        timerData.copy(selectedDays = timerData.selectedDays - day)
                                                                                } else {
                                                                                        timerData.copy(selectedDays = timerData.selectedDays + day)
                                                                                }
                                                                        },
                                                                        label = {
                                                                                Text(
                                                                                        day,
                                                                                        style = MaterialTheme.typography.labelSmall,
                                                                                        maxLines = 1
                                                                                )
                                                                        },
                                                                        modifier = Modifier.weight(1f),
                                                                        colors = FilterChipDefaults.filterChipColors(
                                                                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                                        )
                                                                )
                                                        }
                                                }
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                }
        }
}