package com.arno.timers_compose.feature_periodic_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.arno.timers_compose.R
import androidx.core.net.toUri

object NotificationHelper {
        private const val CHANNEL_ID = "timers_channel"
        private const val CHANNEL_NAME = "Timers"

        fun createChannelIfNeeded(context: Context) {
                val nm =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (nm.getNotificationChannel(CHANNEL_ID) == null) {
                        val ch = NotificationChannel(
                                CHANNEL_ID,
                                CHANNEL_NAME,
                                NotificationManager.IMPORTANCE_DEFAULT
                        )
                        nm.createNotificationChannel(ch)
                }
        }

        fun showNotification(context: Context, id: Int, title: String, text: String) {
                val nm =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notif = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSound("android.resource://${context.packageName}/${R.raw.timer_done}".toUri())
                        .setAutoCancel(true)
                        .build()
                nm.notify(id, notif)
        }
}
