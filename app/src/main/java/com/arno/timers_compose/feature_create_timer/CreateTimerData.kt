package com.arno.timers_compose.feature_create_timer

enum class TimerType {
    DAILY,
    WEEKLY,
    UNLIMITED
}

data class CreateTimerData(
        var name: String = "",
        var hours: Int = 10,
        var minutes: Int = 10,
        var selectedDays: List<String> = emptyList(),
        var timerType: TimerType = TimerType.UNLIMITED
)
