package com.arno.timers_compose.feature_timer_reset

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object TimerResetScheduler {
        private const val RESET_WORKER_NAME = "timer_reset_worker"

        fun scheduleTimerReset(context: Context) {
                val currentTime = Calendar.getInstance()
                val nextMidnight = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                        add(Calendar.DAY_OF_MONTH, 1)
                }

                val delayMillis = nextMidnight.timeInMillis - currentTime.timeInMillis

                val work = PeriodicWorkRequestBuilder<TimerResetWorker>(
                        1, TimeUnit.DAYS
                )
                        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                        .build()

                WorkManager.getInstance(context)
                        .enqueueUniquePeriodicWork(
                                RESET_WORKER_NAME,
                                ExistingPeriodicWorkPolicy.REPLACE,
                                work
                        )
        }

        fun cancelTimerReset(context: Context) {
                WorkManager.getInstance(context).cancelUniqueWork(RESET_WORKER_NAME)
        }
}

