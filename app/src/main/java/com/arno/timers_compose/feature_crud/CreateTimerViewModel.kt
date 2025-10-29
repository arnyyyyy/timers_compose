package com.arno.timers_compose.feature_crud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arno.timers_compose.feature_store_timers.TimerEntity
import com.arno.timers_compose.feature_store_timers.TimerRepository
import kotlinx.coroutines.launch

class CreateTimerViewModel(
        private val timersRepository: TimerRepository
) : ViewModel() {
        fun addTimer(timerData: CreateTimerData) {
                val name = timerData.name
                val hours = timerData.hours.coerceAtLeast(0)
                val minutes =
                        if (hours <= 0 && timerData.minutes <= 0) 1 else timerData.minutes.coerceAtLeast(
                                0
                        )
                val hoursInMillis = hours * 60L * 60L * 1000L
                val minutesInMillis = minutes * 60L * 1000L
                val durationMillis = hoursInMillis + minutesInMillis
                val id = System.currentTimeMillis().toString()
                val newTimer = TimerEntity(
                        id = id,
                        name = name,
                        initialDurationMillis = durationMillis,
                        remainingTimeMillis = durationMillis,
                        hours = hours,
                        minutes = minutes,
                        timerType = timerData.timerType,
                        selectedDays = timerData.selectedDays,
                        isRunning = false,
                        isPaused = true,
                        lastUpdatedTime = 0L,
                        lastStartedTime = 0L,
                )
                viewModelScope.launch {
                        timersRepository.saveTimer(newTimer)
                }
        }

        fun updateTimer(timerData: CreateTimerData, originalTimer: TimerEntity) {
                val name = timerData.name
                val hours = timerData.hours.coerceAtLeast(0)
                val minutes =
                        if (hours <= 0 && timerData.minutes <= 0) 1 else timerData.minutes.coerceAtLeast(
                                0
                        )
                val hoursInMillis = hours * 60L * 60L * 1000L
                val minutesInMillis = minutes * 60L * 1000L
                val durationMillis = hoursInMillis + minutesInMillis

                val updatedTimer = originalTimer.copy(
                        name = name,
                        initialDurationMillis = durationMillis,
                        remainingTimeMillis = if (originalTimer.isRunning || !originalTimer.isPaused) {
                                originalTimer.remainingTimeMillis
                        } else {
                                durationMillis
                        },
                        hours = hours,
                        minutes = minutes,
                        timerType = timerData.timerType,
                        selectedDays = timerData.selectedDays
                )
                viewModelScope.launch {
                        timersRepository.updateTimer(updatedTimer)
                }
        }
}
