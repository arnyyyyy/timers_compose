package com.arno.timers_compose.notifications.feature_live_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.arno.timers_compose.MainActivity
import java.util.Locale
import timerLiveClockView

object TimerLiveUpdateManager {
        private lateinit var notificationManager: NotificationManager
        private lateinit var appContext: Context
        private lateinit var vibrator: Vibrator
        private const val CHANNEL_ID = "timer_live_updates_channel"
        private const val CHANNEL_NAME = "Timer Live Updates"
        private const val CHANNEL_ID_COMPLETED = "timer_completed_channel"
        private const val CHANNEL_NAME_COMPLETED = "Timer Completed"

        private val handler = Handler(Looper.getMainLooper())
        private val updateRunnables = mutableMapOf<String, Runnable>()

        fun initialize(context: Context) {
                appContext = context.applicationContext
                notificationManager =
                        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

                val channel = NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)

                val completedChannel = NotificationChannel(
                        CHANNEL_ID_COMPLETED,
                        CHANNEL_NAME_COMPLETED,
                        NotificationManager.IMPORTANCE_HIGH
                ).apply {
                        enableVibration(true)
                        vibrationPattern = longArrayOf(0, 500, 200, 500, 200, 500)
                        setSound(
                                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                                null
                        )
                        enableLights(true)
                        lightColor = Color.GREEN
                }
                notificationManager.createNotificationChannel(completedChannel)
        }

        fun startTimerLiveUpdate(timerId: String, timerName: String, totalSeconds: Long) {
                cancelTimerLiveUpdate(timerId)

                val startTime = System.currentTimeMillis()
                val endTime = startTime + (totalSeconds * 1000)

                val updateRunnable = object : Runnable {
                        override fun run() {
                                val now = System.currentTimeMillis()
                                val elapsed = now - startTime
                                val remaining = endTime - now

                                if (remaining <= 0) {
                                        showTimerCompleted(timerId, timerName)
                                        updateRunnables.remove(timerId)
                                        return
                                }

                                val progress =
                                        ((elapsed.toFloat() / (totalSeconds * 1000)) * 100).toInt()
                                showTimerProgress(timerId, timerName, progress, remaining / 1000)

                                handler.postDelayed(this, 1000)
                        }
                }

                updateRunnables[timerId] = updateRunnable
                handler.post(updateRunnable)
        }

        fun cancelTimerLiveUpdate(timerId: String) {
                updateRunnables[timerId]?.let { runnable ->
                        handler.removeCallbacks(runnable)
                        updateRunnables.remove(timerId)
                }
                notificationManager.cancel(timerId.hashCode())
        }

        fun cancelAllTimerLiveUpdates() {
                updateRunnables.forEach { (_, runnable) ->
                        handler.removeCallbacks(runnable)
                }
                updateRunnables.clear()
                notificationManager.cancelAll()
        }

        private fun showTimerProgress(
                timerId: String,
                timerName: String,
                progress: Int,
                remainingSeconds: Long
        ) {
                val hours = remainingSeconds / 3600
                val minutes = remainingSeconds % 3600 / 60
                val seconds = remainingSeconds % 60
                val timeText = if (hours > 0)
                        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
                else
                        String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)

                val accentColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        appContext.getColor(android.R.color.system_accent1_500)
                } else {
                        appContext.getColor(android.R.color.holo_blue_dark)
                }

                val largeIcon = timerLiveClockView(progress, timeText, accentColor)

                val builder = NotificationCompat.Builder(appContext, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle(timerName)
                        .setContentText("$timeText remaining")
                        .setSubText("$progress% complete")
                        .setLargeIcon(largeIcon)
                        .setOngoing(true)
                        .setOnlyAlertOnce(true)
                        .setShowWhen(false)
                        .setColorized(true)
                        .setColor(accentColor)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_PROGRESS)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setProgress(100, progress, false)
                        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                        .setContentIntent(createPendingIntent())

                notificationManager.notify(timerId.hashCode(), builder.build())
        }

        private fun showTimerCompleted(timerId: String, timerName: String) {
                val builder = NotificationCompat.Builder(appContext, CHANNEL_ID_COMPLETED)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle("Timer Complete!")
                        .setContentText("$timerName has finished")
                        .setColorized(true)
                        .setColor(Color.rgb(0x4C, 0xAF, 0x50))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setVibrate(longArrayOf(0, 500, 200, 500, 200, 500))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setAutoCancel(true)
                        .setContentIntent(createPendingIntent())

                notificationManager.notify(timerId.hashCode(), builder.build())
        }

        private fun createPendingIntent(): PendingIntent {
                val intent = Intent(appContext, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                return PendingIntent.getActivity(
                        appContext,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
        }
}