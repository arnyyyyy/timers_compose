package com.arno.timers_compose.feature_periodic_notification

import android.content.Context
import java.util.concurrent.TimeUnit
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager

object WorkManagerScheduler {
        private const val UNIQUE_NAME = "timer_periodic_notification_worker"

        fun schedulePeriodic30Min(context: Context) {
                val work = PeriodicWorkRequestBuilder<NotificationWorker>(30, TimeUnit.MINUTES)
                        .build()
                WorkManager.getInstance(context)
                        .enqueueUniquePeriodicWork(
                                UNIQUE_NAME,
                                ExistingPeriodicWorkPolicy.KEEP,
                                work
                        )
        }

        fun cancelPeriodic30Min(context: Context) {
                WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_NAME)
        }
}