package com.arno.timers_compose.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun TimersApp(navController: NavHostController = rememberNavController()) {
        TimerNavHost(navController = navController)
}