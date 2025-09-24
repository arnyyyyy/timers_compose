package com.arno.timers_compose.utils

/**
 * Утилитарный класс для форматирования времени
 */
object TimeFormatter {
    /**
     * Форматирует миллисекунды в строку вида "HH:MM:SS"
     */
    fun formatMillisToTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = millis / (1000 * 60 * 60)

        return buildString {
            if (hours > 0) {
                append(String.format("%02d:", hours))
            }
            append(String.format("%02d:%02d", minutes, seconds))
        }
    }

    /**
     * Конвертирует часы, минуты и секунды в миллисекунды
     */
    fun timeToMillis(hours: Int, minutes: Int, seconds: Int): Long {
        return (hours * 60 * 60 * 1000 + minutes * 60 * 1000 + seconds * 1000).toLong()
    }
}
