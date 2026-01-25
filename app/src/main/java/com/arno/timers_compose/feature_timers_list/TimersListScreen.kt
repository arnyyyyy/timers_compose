package com.arno.timers_compose.feature_timers_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.R
import com.arno.timers_compose.core.AppViewModelProvider
import androidx.compose.runtime.collectAsState
import com.arno.timers_compose.feature_store_timers.TimerCategory
import com.arno.timers_compose.ui.theme.ForestMushroom
import com.arno.timers_compose.ui.theme.ForestOwl
import com.arno.timers_compose.ui.theme.ForestTree

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimersListScreen(
        category: TimerCategory? = null,
        navigateToCreateTimerScreen: (TimerCategory?) -> Unit,
        navigateToTimerDetailScreen: (timerId: String) -> Unit = {},
        onNavigateBack: (() -> Unit)? = null,
        viewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        val timersState = viewModel.timers.collectAsState()
        val allTimers = timersState.value

        val timers = if (category != null) {
                allTimers.filter { it.category == category }
        } else {
                allTimers
        }

        LaunchedEffect(Unit) {
                viewModel.refreshTimers()
        }

        val title = category?.displayName ?: "Мои Таймеры"
        val categoryIcon: @Composable (() -> Unit)? = when (category) {
                TimerCategory.STUDY -> {
                        { ForestOwl(size = 28.dp) }
                }

                TimerCategory.WORK -> {
                        { Text("🦊", fontSize = 28.sp) }
                }

                TimerCategory.REST -> {
                        { Text("🍂", fontSize = 28.sp) }
                }

                TimerCategory.OTHER -> {
                        { ForestMushroom(size = 28.dp) }
                }

                null -> null
        }

        Scaffold(
                topBar = {
                        if (category != null && onNavigateBack != null) {
                                Column {
                                        TopAppBar(
                                                title = {
                                                        Row(
                                                                verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                                categoryIcon?.invoke()
                                                                Spacer(modifier = Modifier.width(12.dp))
                                                                Text(
                                                                        text = title,
                                                                        style = MaterialTheme.typography.titleLarge.copy(
                                                                                fontWeight = FontWeight.Bold
                                                                        )
                                                                )
                                                        }
                                                },
                                                navigationIcon = {
                                                        IconButton(onClick = onNavigateBack) {
                                                                Icon(
                                                                        Icons.AutoMirrored.Filled.ArrowBack,
                                                                        contentDescription = stringResource(R.string.back)
                                                                )
                                                        }
                                                },
                                                colors = TopAppBarDefaults.topAppBarColors(
                                                        containerColor = MaterialTheme.colorScheme.surface
                                                )
                                        )
                                }
                        }
                },
                bottomBar = {
                        Row(
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                                Text("🦊", fontSize = 16.sp)
                                Text("🍂", fontSize = 16.sp)
                                Text("🦔", fontSize = 16.sp)
                                Text("🌰", fontSize = 16.sp)
                                Text("🍂", fontSize = 16.sp)
                                Text("🦊", fontSize = 16.sp)
                        }
                },
                floatingActionButton = {
                        FloatingActionButton(
                                shape = CircleShape,
                                onClick = { navigateToCreateTimerScreen(category) },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                                Icon(
                                        Icons.Default.Add,
                                        contentDescription = stringResource(R.string.add_timer)
                                )
                        }
                },
                containerColor = MaterialTheme.colorScheme.surface
        ) { innerPadding ->
                Box(
                        modifier = Modifier.fillMaxSize()
                ) {
                        if (category == null) {
                                Text(
                                        text = title,
                                        modifier = Modifier
                                                .padding(WindowInsets.statusBars.asPaddingValues())
                                                .align(Alignment.TopCenter)
                                                .padding(top = 16.dp, bottom = 8.dp),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                )
                        }

                        Box(
                                modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .padding(top = if (category == null) 48.dp else 0.dp)
                        ) {
                                if (timers.isEmpty()) {
                                        EmptyTimersCard(category)
                                } else {
                                        LazyColumn(
                                                contentPadding = PaddingValues(
                                                        horizontal = 16.dp,
                                                        vertical = 16.dp
                                                ),
                                                verticalArrangement = Arrangement.spacedBy(16.dp)
                                        ) {
                                                items(timers) { timer ->
                                                        TimerItem(
                                                                timer = timer,
                                                                onClick = {
                                                                        navigateToTimerDetailScreen(
                                                                                timer.id
                                                                        )
                                                                },
                                                                onPlayPauseClick = {
                                                                        viewModel.toggleTimer(timer.id)
                                                                }
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}

@Composable
private fun EmptyTimersCard(category: TimerCategory?) {
        Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {
                Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp
                        )
                ) {
                        Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                // Лесная иконка в зависимости от категории
                                when (category) {
                                        TimerCategory.STUDY -> ForestOwl(size = 64.dp)
                                        TimerCategory.WORK -> Text("🦊", fontSize = 64.sp)
                                        TimerCategory.REST -> Text("🍂", fontSize = 64.sp)
                                        TimerCategory.OTHER -> ForestMushroom(size = 64.dp)
                                        null -> ForestTree(size = 64.dp)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                        text = if (category != null) {
                                                "Нет таймеров в категории\n\"${category.displayName}\""
                                        } else {
                                                "Нет таймеров"
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center
                                )
                                Text(
                                        text = "Нажмите + чтобы создать таймер",
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 8.dp)
                                )
                        }
                }
        }
}
