package com.arno.timers_compose.core

import android.app.Application
import com.arno.timers_compose.feature_timer_reset.TimerResetScheduler
import com.arno.timers_compose.feature_store_timers.TimersContainer
import com.arno.timers_compose.feature_store_timers.TimersDataContainer
import com.arno.timers_compose.notifications.feature_live_notification.TimerLiveUpdateManager

class TimerApplication : Application() {
        lateinit var container: TimersContainer

        override fun onCreate() {
                super.onCreate()
                container = TimersDataContainer(this)

                TimerResetScheduler.initialize(this)
                TimerLiveUpdateManager.initialize(this)
        }
}