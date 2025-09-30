package com.arno.timers_compose.feature_timers_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arno.timers_compose.feature_store_timers.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TimerViewModel(val timersRepository: TimerRepository) : ViewModel() {
        val timers: StateFlow<List<Timer>> =
                timersRepository.getAllTimers().stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000L),
                        initialValue = emptyList()
                )

        private var tickerJob: Job? = null

        fun toggleTimer(id: String) {
                val timer = timers.value.find { it.id == id } ?: return
                if (timer.isRunning) {
                        pauseTimer(timer)
                } else {
                        startTimer(timer)
                }
        }

        private fun startTimer(timer: Timer) {
                val updatedTimer = timer.copy(
                        isRunning = true,
                        isPaused = false,
                        lastStartedTime = System.currentTimeMillis()
                )
                viewModelScope.launch {
                        timersRepository.updateTimer(updatedTimer)
                        startTicker()
                }
        }

        private fun pauseTimer(timer: Timer) {
                val updatedTimer = timer.copy(isRunning = false, isPaused = true)
                viewModelScope.launch {
                        timersRepository.updateTimer(updatedTimer)
                }
                viewModelScope.launch {
                        delay(100)
                        if (timers.value.none { it.isRunning }) {
                                stopTicker()
                        }
                }
        }


        // TODO: создать поле для отображения -- оттуда вычитать красиво пока таймер активен, но
        // поле другое в remain не обновляем
        // обновляем remain только когда пауза или когда вышли из приложения
        // с активным таймером и вернулись
        // даже не обновлять в репе каждый раз а только в UI
        // а в репе только когда пауза или выход из приложения
        
        private fun startTicker() {
                if (tickerJob?.isActive == true) return
                tickerJob = viewModelScope.launch(Dispatchers.Default) {
                        while (true) {
                                delay(1000L)
                                val runningTimers =
                                        timers.value.filter { it.isRunning && it.remainingTimeMillis > 0 }
                                if (runningTimers.isEmpty()) {
                                        break
                                }
                                runningTimers.forEach { timer ->
                                        val newRemaining = timer.remainingTimeMillis - 1000L
                                        val updated = timer.copy(
                                                remainingTimeMillis = newRemaining.coerceAtLeast(0)
                                        )
                                        timersRepository.updateTimer(updated)
                                }
                        }
                }
        }

        fun refreshTimers() {
                Log.d(TAG, "refreshTimers called")
                viewModelScope.launch {
                        val timersList = timersRepository.getAllTimers().firstOrNull()
                        if (timersList.isNullOrEmpty()) {
                                Log.d(TAG, "No timers available yet")
                                return@launch
                        }

                        Log.d(TAG, "Processing ${timersList.size} timers")

                        val runningTimers = timersList.filter { it.isRunning }
                        Log.d(TAG, "Found ${runningTimers.size} running timers to refresh")

                        runningTimers.forEach { timer ->
                                Log.d(TAG, "Refreshing timer: ${timer.id} with ${timer.remainingTimeMillis} ms remaining")
                                val elapsed = System.currentTimeMillis() - timer.lastStartedTime
                                Log.d(TAG, "Elapsed time since last start: $elapsed ms")
                                val newRemaining =
                                        (timer.remainingTimeMillis - elapsed).coerceAtLeast(0)
                                val updated = timer.copy(
                                        remainingTimeMillis = newRemaining,
                                        lastStartedTime = if (newRemaining > 0) System.currentTimeMillis() else 0L,
                                        isRunning = newRemaining > 0,
                                        isPaused = newRemaining == 0L
                                )
                                timersRepository.updateTimer(updated)
                        }

                        if (runningTimers.isNotEmpty()) {
                                startTicker()
                        }
                }
        }

        private fun stopTicker() {
                tickerJob?.cancel()
                tickerJob = null
        }

        companion object {
                const val TAG = "TimerViewModel"
        }
}