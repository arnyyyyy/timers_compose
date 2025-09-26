package com.arno.timers_compose.feature_timers_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arno.timers_compose.feature_create_timer.CreateTimerData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
        private val _timers = MutableStateFlow<List<Timer>>(emptyList())
        val timers: StateFlow<List<Timer>> get() = _timers

        private val timerJobs = mutableMapOf<String, Job>()

        fun addTimer(timerData: CreateTimerData) {
                try {
                        val name = timerData.name.ifBlank { "Мой таймер" }
                        val hours = timerData.hours.coerceAtLeast(0)
                        val minutes =
                                if (hours <= 0 && timerData.minutes <= 0) 1 else timerData.minutes.coerceAtLeast(
                                        0
                                )

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

                        _timers.value = _timers.value + newTimer

                        android.util.Log.d(
                                TAG,
                                "Таймер успешно создан: $name, hours=$hours, minutes=$minutes, durationMillis=$durationMillis"
                        )
                } catch (e: Exception) {
                        android.util.Log.e(TAG, "Ошибка при создании таймера: ${e.message}")
                }
        }

        fun toggleTimer(id: String) {
                val timersList = _timers.value
                val index = timersList.indexOfFirst { it.id == id }
                if (index == -1) return
                val timer = timersList[index]
                if (timer.isRunning) {
                        pauseTimer(timer)
                } else {
                        startTimer(timer)
                }
        }

        private fun startTimer(timer: Timer) {
                val timersList = _timers.value
                val index = timersList.indexOfFirst { it.id == timer.id }
                if (index == -1) return
                val updatedTimer = timer.copy(
                        isRunning = true,
                        isPaused = false,
                        lastStartedTime = System.currentTimeMillis()
                )
                _timers.value = timersList.toMutableList().also { it[index] = updatedTimer }
                timerJobs[timer.id] = viewModelScope.launch {
                        var remaining = timer.remainingTimeMillis
                        while (remaining > 0) {
                                delay(1000)
                                remaining -= 1000
                                val idx = _timers.value.indexOfFirst { it.id == timer.id }
                                if (idx != -1) {
                                        val updated =
                                                _timers.value[idx].copy(remainingTimeMillis = remaining)
                                        _timers.value = _timers.value.toMutableList()
                                                .also { it[idx] = updated }
                                }
                        }
                        val idx = _timers.value.indexOfFirst { it.id == timer.id }
                        if (idx != -1) {
                                val finished = _timers.value[idx].copy(
                                        isRunning = false,
                                        isPaused = true,
                                        remainingTimeMillis = 0
                                )
                                _timers.value =
                                        _timers.value.toMutableList().also { it[idx] = finished }
                        }
                }
        }

        private fun pauseTimer(timer: Timer) {
                val timersList = _timers.value
                val index = timersList.indexOfFirst { it.id == timer.id }
                if (index == -1) return
                val updatedTimer = timer.copy(isRunning = false, isPaused = true)
                _timers.value = timersList.toMutableList().also { it[index] = updatedTimer }
                timerJobs[timer.id]?.cancel()
                timerJobs.remove(timer.id)
        }

        fun resetTimer(id: String) {
                val timersList = _timers.value
                val index = timersList.indexOfFirst { it.id == id }
                if (index == -1) return
                val timer = timersList[index]
                val resetTimer = timer.copy(
                        remainingTimeMillis = timer.initialDurationMillis,
                        isRunning = false,
                        isPaused = true,
                        lastStartedTime = 0L
                )
                _timers.value = timersList.toMutableList().also { it[index] = resetTimer }
                timerJobs[id]?.cancel()
                timerJobs.remove(id)
        }

        fun removeTimer(id: String) {
                _timers.value = _timers.value.filter { it.id != id }
                timerJobs[id]?.cancel()
                timerJobs.remove(id)
        }

        private companion object {
                const val TAG = "TimerViewModel"
        }
}