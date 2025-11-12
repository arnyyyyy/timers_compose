package com.arno.timers_compose.feature_firestore_sync

import android.util.Log
import com.arno.timers_compose.feature_store_timers.TimerEntity
import com.arno.timers_compose.feature_store_timers.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FirestoreSyncManager(
        private val timerRepository: TimerRepository,
        private val firestoreSyncRepository: FirestoreSyncRepository
) {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        suspend fun syncTimerToFirestore(timer: TimerEntity) {
                firestoreSyncRepository.writeTimerToFirestore(timer)
        }

        suspend fun loadTimersFromFirestore() {
                try {
                        val firestoreTimers = firestoreSyncRepository.readTimerFromFirestore()

                        if (firestoreTimers.isNotEmpty()) {
                                timerRepository.insertTimers(firestoreTimers)
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message.toString())
                }
        }

        fun syncTimerInBackground(timer: TimerEntity) {
                scope.launch {
                        try {
                                firestoreSyncRepository.writeTimerToFirestore(timer)
                        } catch (e: Exception) {
                                Log.e(TAG, e.message.toString())
                        }
                }
        }

        suspend fun deleteTimerFromFirestore(timerId: String) {
                firestoreSyncRepository.deleteTimerFromFirestore(timerId)
        }

        companion object {
                private const val TAG = "FirestoreSyncManager"
        }
}

