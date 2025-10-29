package com.arno.timers_compose.feature_timers_list

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arno.timers_compose.feature_periodic_notification.WorkManagerScheduler
import com.arno.timers_compose.feature_store_timers.TimerEntity
import com.arno.timers_compose.feature_store_timers.TimerRepository
import com.arno.timers_compose.utils.DayUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.filter
import kotlin.collections.find

class TimerViewModel(
        val timersRepository: TimerRepository,
        private val context: Context
) : ViewModel() {

        private val _displayTimeTick = MutableStateFlow(0L)

        val timers: StateFlow<List<TimerEntity>> =
                combine(
                        timersRepository.getAllTimers(),
                        _displayTimeTick
                ) { timersFromDb, _ ->
                        timersFromDb
                                .filter { timer ->
                                        DayUtils.shouldShowTimerToday(
                                                timer.timerType,
                                                timer.selectedDays
                                        )
                                }
                                .map { timer ->
                                        if (timer.isRunning) {
                                                val elapsed =
                                                        System.currentTimeMillis() - timer.lastUpdatedTime
                                                val displayTime =
                                                        (timer.remainingTimeMillis - elapsed).coerceAtLeast(
                                                                0
                                                        )
                                                timer.copy(remainingTimeMillis = displayTime)
                                        } else {
                                                timer
                                        }
                                }
                }.stateIn(
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

        private fun startTimer(timer: TimerEntity) {
                val updatedTimer = timer.copy(
                        isRunning = true,
                        isPaused = false,
                        lastUpdatedTime = System.currentTimeMillis(),
                        lastStartedTime = System.currentTimeMillis()

                )
                viewModelScope.launch {
                        timersRepository.updateTimer(updatedTimer)
                        startTicker()
                        checkAndScheduleWorkManager()
                }
        }

        fun pauseTimer(timer: TimerEntity) {
                val elapsed = System.currentTimeMillis() - timer.lastUpdatedTime
                val newRemaining = (timer.remainingTimeMillis - elapsed).coerceAtLeast(0)

                val updatedTimer = timer.copy(
                        isRunning = false,
                        isPaused = true,
                        remainingTimeMillis = newRemaining
                )
                viewModelScope.launch {
                        timersRepository.updateTimer(updatedTimer)
                }
                viewModelScope.launch {
                        delay(100)
                        if (timers.value.none { it.isRunning }) {
                                stopTicker()
                                WorkManagerScheduler.cancelPeriodic30Min(context)
                        }
                }
        }

        private fun startTicker() {
                if (tickerJob?.isActive == true) return
                tickerJob = viewModelScope.launch(Dispatchers.Default) {
                        while (true) {
                                delay(1000L)

                                val hasRunningTimers = timers.value.any { timer ->
                                        if (!timer.isRunning) return@any false
                                        val elapsed =
                                                System.currentTimeMillis() - timer.lastUpdatedTime
                                        val remaining = timer.remainingTimeMillis - elapsed
                                        remaining > 0
                                }

                                if (!hasRunningTimers) {
                                        stopFinishedTimers()
                                        break
                                }

                                _displayTimeTick.value = System.currentTimeMillis()
                        }
                }
        }

        private suspend fun stopFinishedTimers() {
                val currentTimers = timersRepository.getAllTimers().firstOrNull() ?: return

                currentTimers.filter { it.isRunning }.forEach { timer ->
                        val elapsed = System.currentTimeMillis() - timer.lastUpdatedTime
                        val remaining = (timer.remainingTimeMillis - elapsed).coerceAtLeast(0)

                        if (remaining <= 0) {
                                val stoppedTimer = timer.copy(
                                        isRunning = false,
                                        isPaused = true,
                                        remainingTimeMillis = 0
                                )
                                timersRepository.updateTimer(stoppedTimer)
                        }
                }

                val remainingRunning = currentTimers.any { it.isRunning }
                if (!remainingRunning) {
                        WorkManagerScheduler.cancelPeriodic30Min(context)
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
                                Log.d(
                                        TAG,
                                        "Refreshing timer: ${timer.id} with ${timer.remainingTimeMillis} ms remaining"
                                )
                                val elapsed = System.currentTimeMillis() - timer.lastUpdatedTime
                                Log.d(TAG, "Elapsed time since last start: $elapsed ms")
                                val newRemaining =
                                        (timer.remainingTimeMillis - elapsed).coerceAtLeast(0)
                                val updated = timer.copy(
                                        remainingTimeMillis = newRemaining,
                                        lastUpdatedTime = if (newRemaining > 0) System.currentTimeMillis() else 0L,
                                        isRunning = newRemaining > 0,
                                        isPaused = newRemaining == 0L
                                )
                                timersRepository.updateTimer(updated)
                        }

                        if (runningTimers.isNotEmpty()) {
                                startTicker()
                                checkAndScheduleWorkManager()
                        }
                }
        }

        private fun checkAndScheduleWorkManager() {
                viewModelScope.launch {
                        val allTimers = timersRepository.getAllTimers().firstOrNull() ?: emptyList()
                        val hasRunningTimer = allTimers.any { it.isRunning }

                        if (hasRunningTimer) {
                                WorkManagerScheduler.schedulePeriodic30Min(context)
                        }
                }
        }

        private fun stopTicker() {
                tickerJob?.cancel()
                tickerJob = null
        }

        fun deleteTimer(id: String) {
                viewModelScope.launch {
                        timersRepository.deleteTimer(id)
                        if (timers.value.none { it.isRunning }) {
                                stopTicker()
                                WorkManagerScheduler.cancelPeriodic30Min(context)
                        }
                }
        }

        companion object {
                const val TAG = "TimerViewModel"
        }
}