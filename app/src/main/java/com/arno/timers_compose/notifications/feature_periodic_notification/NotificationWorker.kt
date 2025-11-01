package com.arno.timers_compose.notifications.feature_periodic_notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arno.timers_compose.core.TimerApplication
import kotlinx.coroutines.flow.firstOrNull

class NotificationWorker(
        appContext: Context,
        params: WorkerParameters
) : CoroutineWorker(appContext, params) {

        override suspend fun doWork(): Result {
                val app = applicationContext as TimerApplication
                val timerRepository = app.container.timerRepository

                val timers = timerRepository.getAllTimers().firstOrNull() ?: emptyList()
                val hasRunningTimer = timers.any { it.isRunning }

                if (!hasRunningTimer) {
                        return Result.success()
                }

                NotificationHelper.createChannelIfNeeded(applicationContext)
                NotificationHelper.showNotification(
                        applicationContext,
                        id = 1001,
                        title = "Прошло 30 минут",
                        text = "Продолжайте или сделайте перерыв",
                )

                return Result.success()
        }
}