package com.arno.timers_compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arno.timers_compose.feature_create_timer.view.CreateTimerScreen
import com.arno.timers_compose.feature_timers_list.TimersListScreen

object NavRoutes {
        const val TIMERS_LIST = "timers_list"
        const val CREATE_TIMER = "create_timer"
}

@Composable
fun TimerNavHost(
        navController: NavHostController = rememberNavController(),
) {
        val actions = remember(navController) {
                TimerNavigationActions(navController)
        }

        NavHost(
                navController = navController,
                startDestination = NavRoutes.TIMERS_LIST
        ) {
                composable(NavRoutes.TIMERS_LIST) {
                        TimersListScreen(
                                navigateToCreateTimerScreen = actions.navigateToCreateTimer
                        )
                }

                composable(NavRoutes.CREATE_TIMER) {
                        CreateTimerScreen(
                                onNavigateBack = actions.navigateBack
                        )
                }
        }
}

class TimerNavigationActions(private val navController: NavHostController) {
        val navigateToCreateTimer: () -> Unit = {
                try {
                        android.util.Log.d(TAG, "Переход на экран создания таймера")
                        navController.navigate(NavRoutes.CREATE_TIMER)
                } catch (e: Exception) {
                        android.util.Log.e(
                                TAG,
                                "Ошибка при навигации к экрану создания: ${e.message}"
                        )
                }
        }

        val navigateBack: () -> Unit = {
                try {
                        android.util.Log.d(TAG, "Возврат на предыдущий экран")
                        if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                        } else {
                                navController.navigate(NavRoutes.TIMERS_LIST) {
                                        popUpTo(NavRoutes.TIMERS_LIST) { inclusive = true }
                                }
                        }
                } catch (e: Exception) {
                        android.util.Log.e(TAG, "Ошибка при возврате: ${e.message}")
                        try {
                                navController.navigate(NavRoutes.TIMERS_LIST) {
                                        popUpTo(0) { inclusive = true }
                                }
                        } catch (e2: Exception) {
                                android.util.Log.e(
                                        TAG,
                                        "Критическая ошибка навигации: ${e2.message}"
                                )
                        }
                }
        }

        private companion object {
                private const val TAG = "TimerNavigationActions"
        }
}
