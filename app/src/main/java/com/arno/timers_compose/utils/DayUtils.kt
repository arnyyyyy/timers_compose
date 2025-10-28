package com.arno.timers_compose.utils

import com.arno.timers_compose.feature_crud.TimerType
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

object DayUtils {
        fun getCurrentDayOfWeek(): String {
                return LocalDate.now()
                        .dayOfWeek
                        .getDisplayName(TextStyle.FULL, Locale("en"))
                        .lowercase().take(3)
        }


        fun shouldShowTimerToday(
                timerType: TimerType,
                selectedDays: List<String>
        ): Boolean {
                return when (timerType) {
                        TimerType.DAILY -> {
                                if (selectedDays.isEmpty()) {
                                        true
                                } else {
                                        val currentDay = getCurrentDayOfWeek()
                                        selectedDays.any { it.lowercase() == currentDay }
                                }
                        }

                        TimerType.WEEKLY,
                        TimerType.UNLIMITED -> {
                                true
                        }
                }
        }
}
