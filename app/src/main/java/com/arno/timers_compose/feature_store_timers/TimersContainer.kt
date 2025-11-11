package com.arno.timers_compose.feature_store_timers

import android.content.Context
import com.arno.timers_compose.feature_firestore_sync.FirestoreSyncManager
import com.arno.timers_compose.feature_firestore_sync.FirestoreSyncRepository


interface TimersContainer {
        val timerRepository: TimerRepository
        val firestoreSyncManager: FirestoreSyncManager
}

class TimersDataContainer(private val context: Context) : TimersContainer {

        override val timerRepository: TimerRepository by lazy {
                TimerRepository(TimerDatabase.getDatabase(context).timerDao())
        }

        override val firestoreSyncManager: FirestoreSyncManager by lazy {
                FirestoreSyncManager(timerRepository, FirestoreSyncRepository())
        }
}