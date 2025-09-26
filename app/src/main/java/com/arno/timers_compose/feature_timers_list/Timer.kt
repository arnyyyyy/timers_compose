package com.arno.timers_compose.feature_timers_list

import androidx.compose.runtime.Immutable

@Immutable
data class Timer(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    val initialDurationMillis: Long,
    val remainingTimeMillis: Long,
    val hours: Int,
    val minutes: Int,
    val isRunning: Boolean = false,
    val isPaused: Boolean = true,
    val lastStartedTime: Long = 0L
)

// immutable - чтобы Jetpack Compose мог эффективно отслеживать
// изменения состояния и обновлять UI только когда это необходимо