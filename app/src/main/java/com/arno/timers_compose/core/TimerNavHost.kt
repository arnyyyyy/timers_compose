package com.arno.timers_compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arno.timers_compose.feature_auth.AuthScreen
import com.arno.timers_compose.feature_crud.view.CreateTimerScreen
import com.arno.timers_compose.feature_crud.EditTimerScreen
import com.arno.timers_compose.feature_timer_detail.TimerDetailScreen
import com.arno.timers_compose.feature_timers_list.TimersListScreen
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

object NavRoutes {
        const val AUTH = "auth"
        const val TIMERS_LIST = "timers_list"
        const val CREATE_TIMER = "create_timer"
        const val TIMER_DETAIL = "timer_detail/{timerId}"
        const val EDIT_TIMER = "edit_timer/{timerId}"

        fun timerDetail(timerId: String) = "timer_detail/$timerId"
        fun editTimer(timerId: String) = "edit_timer/$timerId"
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
                                navigateToCreateTimerScreen = actions.navigateToCreateTimer,
                                navigateToTimerDetailScreen = actions.navigateToTimerDetail
                        )
                }

                composable(NavRoutes.CREATE_TIMER) {
                        CreateTimerScreen(
                                onNavigateBack = actions.navigateBack
                        )
                }

                composable(
                        route = NavRoutes.TIMER_DETAIL,
                        arguments = listOf(navArgument("timerId") { type = NavType.StringType })
                ) { backStackEntry ->
                        TimerDetailScreen(
                                timerId = backStackEntry.arguments?.getString("timerId") ?: "",
                                onNavigateBack = actions.navigateBack,
                                onNavigateToEdit = actions.navigateToEditTimer
                        )
                }

                composable(
                        route = NavRoutes.EDIT_TIMER,
                        arguments = listOf(navArgument("timerId") { type = NavType.StringType })
                ) { backStackEntry ->
                        EditTimerScreen(
                                timerId = backStackEntry.arguments?.getString("timerId") ?: "",
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

        val navigateToTimerDetail: (String) -> Unit = { timerId ->
                try {
                        navController.navigate(NavRoutes.timerDetail(timerId))
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                }
        }

        val navigateToEditTimer: (String) -> Unit = { timerId ->
                try {
                        navController.navigate(NavRoutes.editTimer(timerId))
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