package com.arno.timers_compose.feature_store_timers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TimerRepository(private val timerDao: TimerDao) {
        fun getAllTimers(): Flow<List<TimerEntity>> {
                return timerDao.getAllTimers().map { it -> it.map { entity -> entity } }
        }

        fun getTimerById(id: String): Flow<TimerEntity?> {
                return timerDao.getTimerById(id).map { it }
        }

        suspend fun saveTimer(timer: TimerEntity) {
                timerDao.insertTimer(timer)
        }

        suspend fun updateTimer(timer: TimerEntity) {
                timerDao.updateTimer(timer)
        }

        suspend fun deleteTimer(id: String) {
                timerDao.deleteTimerById(id)
        }

        suspend fun deleteAllTimers() {
                timerDao.deleteAllTimers()
        }

        suspend fun insertTimers(timers: List<TimerEntity>) {
                timerDao.insertTimers(timers)
        }
}