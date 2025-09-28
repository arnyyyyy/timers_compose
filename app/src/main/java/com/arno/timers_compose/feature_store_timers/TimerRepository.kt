package com.arno.timers_compose.feature_store_timers

import com.arno.timers_compose.feature_timers_list.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TimerRepository(private val timerDao: TimerDao) {
        fun getAllTimers(): Flow<List<Timer>> {
                return timerDao.getAllTimers().map { it -> it.map { entity -> entity.toDomain() } }
        }

        fun getTimerById(id: String): Flow<Timer?> {
                return timerDao.getTimerById(id).map { it.toDomain() }
        }

        suspend fun saveTimer(timer: Timer) {
                timerDao.insertTimer(timer.toEntity())
        }

        suspend fun updateTimer(timer: Timer) {
                timerDao.updateTimer(timer.toEntity())
        }

        suspend fun deleteTimer(id: String) {
                timerDao.deleteTimerById(id)
        }

        suspend fun deleteAllTimers() {
                timerDao.deleteAllTimers()
        }
}