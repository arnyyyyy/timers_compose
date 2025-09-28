package com.arno.timers_compose.feature_store_timers

import android.content.Context


interface TimersContainer {
    val timerRepository: TimerRepository
}

class TimersDataContainer(private val context: Context) : TimersContainer {

    override val timerRepository: TimerRepository by lazy {
        TimerRepository(TimerDatabase.getDatabase(context).timerDao())
    }
}