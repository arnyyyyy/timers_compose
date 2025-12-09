package com.arno.timers_compose.feature_timer_reset

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arno.timers_compose.core.TimerApplication
import com.arno.timers_compose.feature_crud.TimerType
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar

class TimerResetWorker(
        appContext: Context,
        params: WorkerParameters
) : CoroutineWorker(appContext, params) {

        override suspend fun doWork(): Result {
                return try {
                        val app = applicationContext as TimerApplication
                        val timerRepository = app.container.timerRepository
                        val firestoreSyncManager = app.container.firestoreSyncManager

                        val timers = timerRepository.getAllTimers().firstOrNull() ?: emptyList()
                        val calendar = Calendar.getInstance()
                        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                        val isMonday = currentDayOfWeek == Calendar.MONDAY

                        timers.forEach { timer ->
                                val shouldReset = when (timer.timerType) {
                                        TimerType.DAILY -> true
                                        TimerType.WEEKLY -> isMonday
                                        TimerType.UNLIMITED -> false
                                }

                                if (shouldReset) {
                                        val resetTimer = timer.copy(
                                                remainingTimeMillis = timer.initialDurationMillis,
                                                isRunning = false,
                                                isPaused = false,
                                                lastUpdatedTime = 0L,
                                                lastStartedTime = 0L,
                                        )
                                        timerRepository.updateTimer(resetTimer)
                                        try {
                                                firestoreSyncManager.syncTimerInBackground(resetTimer)
                                        } catch (e: Exception) {
                                                Log.e(TAG, "Failed to sync reset timer ${timer.id}", e)
                                        }
                                }
                        }

                        Result.success()
                } catch (e: Exception) {
                        Log.e(TAG, e.toString())
                        Result.retry()
                }
        }

        companion object {
                private const val TAG = "TimerResetWorker"
        }
}
