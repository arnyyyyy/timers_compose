package com.arno.timers_compose.feature_store_timers

enum class TimerCategory(
        val displayName: String,
        val emoji: String
) {
        STUDY("Учёба", "📚"),
        WORK("Работа", "💼"),
        REST("Отдых", "🌿"),
        OTHER("Другое", "🌲");

        companion object {
                fun fromString(value: String?): TimerCategory {
                        return entries.find { it.name == value } ?: OTHER
                }
        }
}

