package com.arno.timers_compose.feature_create_timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CreateTimerData {
        var name by mutableStateOf("")
        var hours by mutableIntStateOf(10)
        var minutes by mutableIntStateOf(10)
}

