package com.arno.timers_compose.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arno.timers_compose.feature_auth.view.AuthScreen
import com.arno.timers_compose.feature_crud.view.CreateTimerScreen
import com.arno.timers_compose.feature_crud.view.EditTimerScreen
import com.arno.timers_compose.feature_home.HomeScreen
import com.arno.timers_compose.feature_store_timers.TimerCategory
import com.arno.timers_compose.feature_timer_detail.TimerDetailScreen
import com.arno.timers_compose.feature_timers_list.TimersListScreen
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

object NavRoutes {
        const val AUTH = "auth"
        const val HOME = "home"
        const val TIMERS_LIST = "timers_list"
        const val CATEGORY_TIMERS = "category_timers/{category}"
        const val CREATE_TIMER = "create_timer"
        const val CREATE_TIMER_WITH_CATEGORY = "create_timer/{category}"
        const val TIMER_DETAIL = "timer_detail/{timerId}"
        const val EDIT_TIMER = "edit_timer/{timerId}"

        fun timerDetail(timerId: String) = "timer_detail/$timerId"
        fun editTimer(timerId: String) = "edit_timer/$timerId"
        fun categoryTimers(category: TimerCategory) = "category_timers/${category.name}"
        fun createTimerWithCategory(category: TimerCategory?) =
                if (category != null) "create_timer/${category.name}" else CREATE_TIMER
}

@Composable
fun TimerNavHost(
        navController: NavHostController = rememberNavController(),
) {
        val context = LocalContext.current
        val userPreferences = remember { UserPreferencesManager(context) }

        val actions = remember(navController) {
                TimerNavigationActions(navController)
        }

        val isAuthenticated = FirebaseAuth.getInstance().currentUser != null
        val isAuthSkipped = userPreferences.isAuthSkipped()

        val startDestination = if (isAuthenticated || isAuthSkipped) {
                NavRoutes.HOME
        } else {
                NavRoutes.AUTH
        }

        NavHost(
                navController = navController,
                startDestination = startDestination
        ) {
                composable(NavRoutes.AUTH) {
                        AuthScreen(
                                onAuthSuccess = actions.navigateToHome
                        )
                }

                composable(NavRoutes.HOME) {
                        HomeScreen(
                                onCategoryClick = actions.navigateToCategoryTimers
                        )
                }

                composable(NavRoutes.TIMERS_LIST) {
                        TimersListScreen(
                                category = null,
                                navigateToCreateTimerScreen = { actions.navigateToCreateTimer(null) },
                                navigateToTimerDetailScreen = actions.navigateToTimerDetail
                        )
                }

                composable(
                        route = NavRoutes.CATEGORY_TIMERS,
                        arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("category") ?: ""
                        val category = TimerCategory.fromString(categoryName)
                        TimersListScreen(
                                category = category,
                                navigateToCreateTimerScreen = { actions.navigateToCreateTimer(category) },
                                navigateToTimerDetailScreen = actions.navigateToTimerDetail,
                                onNavigateBack = actions.navigateBack
                        )
                }

                composable(NavRoutes.CREATE_TIMER) {
                        CreateTimerScreen(
                                onNavigateBack = actions.navigateBack,
                                initialCategory = null
                        )
                }

                composable(
                        route = NavRoutes.CREATE_TIMER_WITH_CATEGORY,
                        arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("category") ?: ""
                        val category = TimerCategory.fromString(categoryName)
                        CreateTimerScreen(
                                onNavigateBack = actions.navigateBack,
                                initialCategory = category
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

private const val TAG = "TimerNavigation"

class TimerNavigationActions(private val navController: NavHostController) {
        val navigateToHome: () -> Unit = {
                try {
                        navController.navigate(NavRoutes.HOME) {
                                popUpTo(NavRoutes.AUTH) { inclusive = true }
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                }
        }

        val navigateToTimersList: () -> Unit = {
                try {
                        navController.navigate(NavRoutes.TIMERS_LIST) {
                                popUpTo(NavRoutes.AUTH) { inclusive = true }
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                }
        }

        val navigateToCategoryTimers: (TimerCategory) -> Unit = { category ->
                try {
                        navController.navigate(NavRoutes.categoryTimers(category))
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                }
        }

        val navigateToCreateTimer: (TimerCategory?) -> Unit = { category ->
                try {
                        navController.navigate(NavRoutes.createTimerWithCategory(category))
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
                                navController.navigate(NavRoutes.HOME) {
                                        popUpTo(NavRoutes.HOME) { inclusive = true }
                                }
                        }
                } catch (e: Exception) {
                        Log.e(TAG, e.message ?: "")
                        try {
                                navController.navigate(NavRoutes.HOME) {
                                        popUpTo(0) { inclusive = true }
                                }
                        } catch (e2: Exception) {
                                Log.e(TAG, e2.message ?: "")
                        }
                }
        }
}