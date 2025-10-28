package com.arno.timers_compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arno.timers_compose.feature_auth.AuthScreen
import com.arno.timers_compose.feature_create_timer.view.CreateTimerScreen
import com.arno.timers_compose.feature_timers_list.TimersListScreen
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

object NavRoutes {
        const val AUTH = "auth"
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

        val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
                NavRoutes.TIMERS_LIST
        } else {
                NavRoutes.AUTH
        }

        NavHost(
                navController = navController,
                startDestination = startDestination
        ) {
                composable(NavRoutes.AUTH) {
                        AuthScreen(
                                onAuthSuccess = actions.navigateToTimersList
                        )
                }

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
        val navigateToTimersList: () -> Unit = {
                try {
                        navController.navigate(NavRoutes.TIMERS_LIST) {
                                popUpTo(NavRoutes.AUTH) { inclusive = true }
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                }
        }

        val navigateToCreateTimer: () -> Unit = {
                try {
                        navController.navigate(NavRoutes.CREATE_TIMER)
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                }
        }

        val navigateBack: () -> Unit = {
                try {
                        if (navController.previousBackStackEntry != null) {
                                navController.popBackStack()
                        } else {
                                navController.navigate(NavRoutes.TIMERS_LIST) {
                                        popUpTo(NavRoutes.TIMERS_LIST) { inclusive = true }
                                }
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                        try {
                                navController.navigate(NavRoutes.TIMERS_LIST) {
                                        popUpTo(0) { inclusive = true }
                                }
                        } catch (e2: Exception) {
                                Log.e(TAG, e2.message ?: "")
                        }
                }
        }

        private companion object {
                private const val TAG = "TimerNavigationActions"
        }
}
