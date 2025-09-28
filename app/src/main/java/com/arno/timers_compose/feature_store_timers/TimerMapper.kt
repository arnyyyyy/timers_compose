package com.arno.timers_compose.feature_store_timers

import com.arno.timers_compose.feature_timers_list.Timer

fun Timer.toEntity() = TimerEntity(
        id = id,
        name = name,
        initialDurationMillis = initialDurationMillis,
        remainingTimeMillis = remainingTimeMillis,
        hours = hours,
        minutes = minutes,
        isRunning = isRunning,
        isPaused = isPaused,
        lastStartedTime = lastStartedTime
)

fun TimerEntity.toDomain() = Timer(
        id = id,
        name = name,
        initialDurationMillis = initialDurationMillis,
        remainingTimeMillis = remainingTimeMillis,
        hours = hours,
        minutes = minutes,
        isRunning = isRunning,
        isPaused = isPaused,
        lastStartedTime = lastStartedTime
)
