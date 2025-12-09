package com.arno.timers_compose.core

import android.app.Application
import com.arno.timers_compose.feature_timer_reset.TimerResetScheduler
import com.arno.timers_compose.notifications.feature_live_notification.TimerLiveUpdateManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.arno.timers_compose.feature_store_timers.TimersContainer

@HiltAndroidApp
class TimerApplication : Application() {
        @Inject
        lateinit var container: TimersContainer

        override fun onCreate() {
                super.onCreate()

                TimerResetScheduler.initialize(this)
                TimerLiveUpdateManager.initialize(this)
        }
}