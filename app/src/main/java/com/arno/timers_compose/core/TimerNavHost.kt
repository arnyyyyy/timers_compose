package com.arno.timers_compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arno.timers_compose.feature_show_timer.Timer
import com.arno.timers_compose.feature_create_timer.CreateTimerScreen
import com.arno.timers_compose.feature_show_timer.MainScreen

object NavRoutes {
        const val TIMERS_LIST = "timers_list"
        const val CREATE_TIMER = "create_timer"
}

@Composable
fun TimerNavHost(
        navController: NavHostController = rememberNavController(),
        timers: List<Timer>,
        onTimerClick: (String) -> Unit,
        onAddTimer: (String, Long) -> Unit
) {
        val actions = remember(navController) {
                TimerNavigationActions(navController)
        }

        NavHost(
                navController = navController,
                startDestination = NavRoutes.TIMERS_LIST
        ) {
                composable(NavRoutes.TIMERS_LIST) {
                        MainScreen(
                                timers = timers,
                                onTimerClick = onTimerClick,
                                onAddClick = actions.navigateToCreateTimer
                        )
                }

                composable(NavRoutes.CREATE_TIMER) {
                        CreateTimerScreen(
                                onCreateTimer = { name, durationMillis ->
                                        onAddTimer(name, durationMillis)
                                        actions.navigateBack()
                                },
                                onNavigateBack = actions.navigateBack
                        )
                }
        }
}

/**
 * Действия навигации
 */
class TimerNavigationActions(private val navController: NavHostController) {
        val navigateToCreateTimer: () -> Unit = {
                navController.navigate(NavRoutes.CREATE_TIMER)
        }

        val navigateBack: () -> Unit = {
                navController.popBackStack()
        }
}
