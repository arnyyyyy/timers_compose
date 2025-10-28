package com.arno.timers_compose.core

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.arno.timers_compose.feature_create_timer.CreateTimerViewModel
import com.arno.timers_compose.feature_timers_list.TimerViewModel

object AppViewModelProvider {
        val Factory = viewModelFactory {
                initializer {
                        TimerViewModel(
                                timerApplication().container.timerRepository,
                                timerApplication().applicationContext
                        )
                }
                initializer {
                        CreateTimerViewModel(
                                timerApplication().container.timerRepository
                        )
                }
        }
}

fun CreationExtras.timerApplication(): TimerApplication =
        (this[AndroidViewModelFactory.APPLICATION_KEY] as TimerApplication)