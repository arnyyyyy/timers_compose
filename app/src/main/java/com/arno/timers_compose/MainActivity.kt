package com.arno.timers_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arno.timers_compose.core.TimersApp
import com.arno.timers_compose.ui.theme.Timers_composeTheme

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                enableEdgeToEdge()
                setContent {
                        Timers_composeTheme {
                                TimersApp()
                        }
                }
        }
}
