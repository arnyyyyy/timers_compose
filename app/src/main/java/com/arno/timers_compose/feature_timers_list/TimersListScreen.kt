package com.arno.timers_compose.feature_timers_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.R
import com.arno.timers_compose.core.AppViewModelProvider
import androidx.compose.runtime.collectAsState

@Composable
fun TimersListScreen(
        navigateToCreateTimerScreen: () -> Unit,
        navigateToTimerDetailScreen: (timerId: String) -> Unit = {},
        viewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        val timersState = viewModel.timers.collectAsState()
        val timers = timersState.value

        LaunchedEffect(Unit) {
                viewModel.refreshTimers()
        }

        val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                        Color(0xFF88CCFF).copy(alpha = 0.7f),
                        Color(0xFFCCDDFF).copy(alpha = 0.6f)
                )
        )

        Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                        FloatingActionButton(
                                shape = CircleShape,
                                onClick = navigateToCreateTimerScreen,
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                contentColor = Color.White,
                                elevation = FloatingActionButtonDefaults.elevation(0.dp)
                        ) {
                                Icon(
                                        Icons.Default.Add,
                                        contentDescription = stringResource(R.string.add_timer)
                                )
                        }
                }
        ) { innerPadding ->
                Box(
                        modifier = Modifier
                                .fillMaxSize()
                ) {
                        Box(
                                modifier = Modifier
                                        .fillMaxSize()
                                        .background(gradientBrush)
                        )

                        Text(
                                text = "Мои Таймеры",
                                modifier = Modifier
                                        .padding(WindowInsets.statusBars.asPaddingValues())
                                        .align(Alignment.TopCenter)
                                        .padding(top = 16.dp, bottom = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color(0xFF283458)
                        )

                        Box(
                                modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                        .padding(top = 48.dp)
                        ) {
                                if (timers.isEmpty()) {
                                        Card(
                                                modifier = Modifier
                                                        .padding(16.dp)
                                                        .align(Alignment.Center),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                        containerColor = Color.White.copy(alpha = 0.7f)
                                                ),
                                        ) {
                                                Column(
                                                        modifier = Modifier.padding(24.dp),
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                        Text(
                                                                text = "Нет таймеров",
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
