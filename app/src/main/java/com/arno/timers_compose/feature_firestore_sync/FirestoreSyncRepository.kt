package com.arno.timers_compose.feature_firestore_sync

import android.util.Log
import com.arno.timers_compose.feature_crud.TimerType
import com.arno.timers_compose.feature_store_timers.TimerEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreSyncRepository {
        private val firestore = FirebaseFirestore.getInstance()
        private val auth = FirebaseAuth.getInstance()

        private fun getUserTimersCollection() = auth.currentUser?.let { user ->
                firestore.collection("users").document(user.uid).collection("timers")
        }

        suspend fun syncTimerToFirestore(timer: TimerEntity) {
                try {
                        val collection = getUserTimersCollection() ?: return
                        val timerMap = mapOf(
                                "id" to timer.id,
                                "name" to timer.name,
                                "initialDurationMillis" to timer.initialDurationMillis,
                                "remainingTimeMillis" to timer.remainingTimeMillis,
                                "hours" to timer.hours,
                                "minutes" to timer.minutes,
                                "timerType" to timer.timerType.name,
                                "selectedDays" to timer.selectedDays,
                                "isRunning" to timer.isRunning,
                                "isPaused" to timer.isPaused,
                                "lastUpdatedTime" to timer.lastUpdatedTime,
                                "lastStartedTime" to timer.lastStartedTime
                        )
                        collection.document(timer.id).set(timerMap, SetOptions.merge()).await()
                } catch (e: Exception) {
                }
        }

        suspend fun loadTimersFromFirestore(): List<TimerEntity> {
                return try {
                        val collection = getUserTimersCollection() ?: return emptyList()
                        val snapshot = collection.get().await()

                        snapshot.documents.mapNotNull { doc ->
                                try {
                                        TimerEntity(
                                                id = doc.getString("id") ?: return@mapNotNull null,
                                                name = doc.getString("name") ?: "",
                                                initialDurationMillis = doc.getLong("initialDurationMillis")
                                                        ?: 0L,
                                                remainingTimeMillis = doc.getLong("remainingTimeMillis")
                                                        ?: 0L,
                                                hours = doc.getLong("hours")?.toInt() ?: 0,
                                                minutes = doc.getLong("minutes")?.toInt() ?: 0,
                                                timerType = doc.getString("timerType")?.let {
                                                        TimerType.valueOf(it)
                                                } ?: TimerType.DAILY,
                                                selectedDays = (doc.get("selectedDays") as? List<*>)?.mapNotNull { it as? String }
                                                        ?: emptyList(),
                                                isRunning = doc.getBoolean("isRunning") ?: false,
                                                isPaused = doc.getBoolean("isPaused") ?: true,
                                                lastUpdatedTime = doc.getLong("lastUpdatedTime")
                                                        ?: 0L,
                                                lastStartedTime = doc.getLong("lastStartedTime")
                                                        ?: 0L
                                        )
                                } catch (e: Exception) {
                                        Log.e(TAG, e.message.toString())
                                        null
                                }
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message.toString())
                        emptyList()
                }
        }

        suspend fun deleteTimerFromFirestore(timerId: String) {
                try {
                        val collection = getUserTimersCollection() ?: return
                        collection.document(timerId).delete().await()
                } catch (_: Exception) {
                }
        }

        companion object {
                private const val TAG = "FirestoreSyncRepository"
        }
}

