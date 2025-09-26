package com.arno.timers_compose.utils

import java.util.Locale

object TimeFormatter {
        fun formatMillis(millis: Long): String {
                val totalSeconds = millis / 1000
                val hours = totalSeconds / 3600
                val minutes = if (hours > 0) (totalSeconds % 3600) / 60 else totalSeconds / 60
                val seconds = totalSeconds % 60
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        }
}
