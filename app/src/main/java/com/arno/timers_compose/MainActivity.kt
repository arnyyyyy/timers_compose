package com.arno.timers_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arno.timers_compose.ui.theme.Timers_composeTheme
import com.arno.timers_compose.feature_show_timer.TimerViewModel
import com.arno.timers_compose.core.TimerNavHost

class MainActivity : ComponentActivity() {
    private val timerViewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Timers_composeTheme {
                val timers by timerViewModel.timers.collectAsState()

                TimerNavHost(
                    timers = timers,
                    onTimerClick = { id -> timerViewModel.toggleTimer(id) },
                    onAddTimer = { name, durationMillis ->
                        timerViewModel.addTimer(name, durationMillis)
                    }
                )
            }
        }
    }
}
