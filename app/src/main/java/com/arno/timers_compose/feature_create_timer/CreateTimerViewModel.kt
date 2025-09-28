package com.arno.timers_compose.feature_create_timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arno.timers_compose.feature_store_timers.TimerRepository
import com.arno.timers_compose.feature_timers_list.Timer
import kotlinx.coroutines.launch

class CreateTimerViewModel(
    private val timersRepository: TimerRepository
) : ViewModel() {
    fun addTimer(timerData: CreateTimerData) {
        val name = timerData.name
        val hours = timerData.hours.coerceAtLeast(0)
        val minutes = if (hours <= 0 && timerData.minutes <= 0) 1 else timerData.minutes.coerceAtLeast(0)
        val hoursInMillis = hours * 60L * 60L * 1000L
        val minutesInMillis = minutes * 60L * 1000L
        val durationMillis = hoursInMillis + minutesInMillis
        val id = System.currentTimeMillis().toString()
        val newTimer = Timer(
            id = id,
            name = name,
            initialDurationMillis = durationMillis,
            remainingTimeMillis = durationMillis,
            hours = hours,
            minutes = minutes,
            isRunning = false,
            isPaused = true,
            lastStartedTime = 0L
        )
        viewModelScope.launch {
            timersRepository.saveTimer(newTimer)
        }
    }
}

