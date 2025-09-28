package com.arno.timers_compose.feature_store_timers

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
        @Query("SELECT * FROM timers")
        fun getAllTimers(): Flow<List<TimerEntity>>

        @Query("SELECT * FROM timers WHERE id = :id")
        fun getTimerById(id: String): Flow<TimerEntity>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTimer(timer: TimerEntity)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertTimers(timers: List<TimerEntity>)

        @Update
        suspend fun updateTimer(timer: TimerEntity)

        @Delete
        suspend fun deleteTimer(timer: TimerEntity)

        @Query("DELETE FROM timers WHERE id = :id")
        suspend fun deleteTimerById(id: String)

        @Query("DELETE FROM timers")
        suspend fun deleteAllTimers()
}