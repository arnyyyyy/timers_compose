package com.arno.timers_compose.feature_crud

import com.arno.timers_compose.feature_store_timers.TimerCategory

enum class TimerType {
        DAILY,
        WEEKLY,
        UNLIMITED
}

data class CreateTimerData(
        var name: String = "Мой таймер",
        var hours: Int = 10,
        var minutes: Int = 10,
        var selectedDays: List<String> = emptyList(),
        var timerType: TimerType = TimerType.UNLIMITED,
        var category: TimerCategory = TimerCategory.OTHER
)
