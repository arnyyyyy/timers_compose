package com.arno.timers_compose.feature_crud.view

import android.widget.TimePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arno.timers_compose.R
import com.arno.timers_compose.feature_crud.CreateTimerData
import com.arno.timers_compose.feature_crud.TimerType

@Composable
fun TimerFormContent(
        timerData: CreateTimerData,
        onTimerDataChange: (CreateTimerData) -> Unit,
        modifier: Modifier = Modifier
) {
        val haptic = LocalHapticFeedback.current
        val currentTimerData by rememberUpdatedState(newValue = timerData)

        Column(
                modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
                TimerNameCard(
                        name = timerData.name,
                        onNameChange = { onTimerDataChange(currentTimerData.copy(name = it)) }
                )

                TimerDurationCard(
                        hours = timerData.hours,
                        minutes = timerData.minutes,
                        onTimeChange = { hours, minutes ->
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onTimerDataChange(
                                        currentTimerData.copy(
                                                hours = hours,
                                                minutes = minutes
                                        )
                                )
                        }
                )

                TimerTypeCard(
                        selectedType = timerData.timerType,
                        onTypeChange = { onTimerDataChange(currentTimerData.copy(timerType = it)) }
                )

                if (timerData.timerType == TimerType.DAILY) {
                        TimerWeekDaysCard(
                                selectedDays = timerData.selectedDays,
                                onDaysChange = {
                                        onTimerDataChange(
                                                currentTimerData.copy(
                                                        selectedDays = it
                                                )
                                        )
                                }
                        )
                }

                Spacer(modifier = Modifier.height(24.dp))
        }
}

@Composable
private fun TimerNameCard(
        name: String,
        onNameChange: (String) -> Unit
) {
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
                                value = name,
                                onValueChange = onNameChange,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Мой таймер") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(
                                                alpha = 0.3f
                                        )
                                )
                        )
                }
        }
}

@Composable
private fun TimerDurationCard(
        hours: Int,
        minutes: Int,
        onTimeChange: (Int, Int) -> Unit
) {
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
                                                hour = hours
                                                minute = minutes
                                                setOnTimeChangedListener { _, hourOfDay, minute ->
                                                        onTimeChange(hourOfDay, minute)
                                                }
                                        }
                                },
                                update = { timePicker ->
                                        if (timePicker.hour != hours) timePicker.hour = hours
                                        if (timePicker.minute != minutes) timePicker.minute =
                                                minutes
                                },
                                modifier = Modifier
                                        .padding(bottom = 24.dp)
                                        .size(250.dp, 250.dp)
                        )
                }
        }
}

@Composable
private fun TimerTypeCard(
        selectedType: TimerType,
        onTypeChange: (TimerType) -> Unit
) {
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
                                                selected = selectedType == type,
                                                onClick = { onTypeChange(type) },
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
}

@Composable
private fun TimerWeekDaysCard(
        selectedDays: List<String>,
        onDaysChange: (List<String>) -> Unit
) {
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
                                listOf(
                                        "Mon",
                                        "Tue",
                                        "Wed",
                                        "Thu",
                                        "Fri",
                                        "Sat",
                                        "Sun"
                                ).forEach { day ->
                                        val isSelected = selectedDays.contains(day)
                                        FilterChip(
                                                selected = isSelected,
                                                onClick = {
                                                        onDaysChange(
                                                                if (isSelected) {
                                                                        selectedDays - day
                                                                } else {
                                                                        selectedDays + day
                                                                }
                                                        )
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
