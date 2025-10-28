package com.arno.timers_compose.feature_store_timers

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arno.timers_compose.feature_crud.TimerType

@Entity(tableName = "timers")
data class TimerEntity(
        @PrimaryKey val id: String,
        val name: String,
        val initialDurationMillis: Long,
        val remainingTimeMillis: Long,
        val hours: Int,
        val minutes: Int,
        val timerType: TimerType,
        val selectedDays: List<String> = emptyList(),
        val isRunning: Boolean,
        val isPaused: Boolean,
        val lastStartedTime: Long
)