package com.arno.timers_compose.core

import android.app.Application
import com.arno.timers_compose.feature_store_timers.TimersContainer
import com.arno.timers_compose.feature_store_timers.TimersDataContainer

class TimerApplication : Application() {
        lateinit var container: TimersContainer

        override fun onCreate() {
                super.onCreate()
                container = TimersDataContainer(this)
        }
}