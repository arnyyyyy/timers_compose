package com.arno.timers_compose.feature_home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.core.AppViewModelProvider
import com.arno.timers_compose.feature_store_timers.TimerCategory
import com.arno.timers_compose.feature_store_timers.TimerEntity
import com.arno.timers_compose.feature_timers_list.TimerViewModel
import com.arno.timers_compose.ui.theme.*
import com.arno.timers_compose.utils.TimeFormatter.formatMillis

@Composable
fun HomeScreen(
        onCategoryClick: (TimerCategory) -> Unit,
        viewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        val timersState = viewModel.timers.collectAsState()
        val timers = timersState.value

        LaunchedEffect(Unit) {
                viewModel.refreshTimers()
        }

        val totalGoalTimeToday = timers.sumOf { it.initialDurationMillis }
        val totalSpentTimeToday = timers.sumOf { it.initialDurationMillis - it.remainingTimeMillis }
        val progress = if (totalGoalTimeToday > 0) {
                (totalSpentTimeToday.toFloat() / totalGoalTimeToday.toFloat()).coerceIn(0f, 1f)
        } else 0f

        val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(500),
                label = "progress"
        )

        val timersByCategory = timers.groupBy { it.category }

        Scaffold(
                containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
                Column(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                        ) {
                                ForestTree(size = 32.dp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                        text = "Лесные Таймеры",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                ForestTree(size = 32.dp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        TodayProgressCircle(
                                totalGoalTime = totalGoalTimeToday,
                                totalSpentTime = totalSpentTimeToday,
                                progress = animatedProgress,
                                hasRunningTimer = timers.any { it.isRunning }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                                Text("🦊", fontSize = 36.sp)
                                Text("🦔", fontSize = 36.sp)
                                ForestOwl(size = 36.dp)
                                Text("🐰", fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                                text = "Категории",
                                style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                        )

                        Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                        CategoryCard(
                                                category = TimerCategory.STUDY,
                                                timers = timersByCategory[TimerCategory.STUDY] ?: emptyList(),
                                                onClick = { onCategoryClick(TimerCategory.STUDY) },
                                                modifier = Modifier.weight(1f),
                                                icon = { ForestOwl(size = 40.dp) },
                                                accentColor = Color(0xFF5C6BC0)
                                        )
                                        CategoryCard(
                                                category = TimerCategory.WORK,
                                                timers = timersByCategory[TimerCategory.WORK] ?: emptyList(),
                                                onClick = { onCategoryClick(TimerCategory.WORK) },
                                                modifier = Modifier.weight(1f),
                                                icon = { Text("🦊", fontSize = 40.sp) },
                                                accentColor = Color(0xFFFF7043)
                                        )
                                }
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                        CategoryCard(
                                                category = TimerCategory.REST,
                                                timers = timersByCategory[TimerCategory.REST] ?: emptyList(),
                                                onClick = { onCategoryClick(TimerCategory.REST) },
                                                modifier = Modifier.weight(1f),
                                                icon = { Text("🍂", fontSize = 40.sp) },
                                                accentColor = Color(0xFF66BB6A)
                                        )
                                        CategoryCard(
                                                category = TimerCategory.OTHER,
                                                timers = timersByCategory[TimerCategory.OTHER] ?: emptyList(),
                                                onClick = { onCategoryClick(TimerCategory.OTHER) },
                                                modifier = Modifier.weight(1f),
                                                icon = { ForestMushroom(size = 40.dp) },
                                                accentColor = Color(0xFFAB47BC)
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                                Text("🌰", fontSize = 28.sp)
                                Text("🍂", fontSize = 28.sp)
                                ForestMushroom(size = 28.dp)
                                Text("🌰", fontSize = 28.sp)
                                Text("🍂", fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                }
        }
}

@Composable
private fun TodayProgressCircle(
        totalGoalTime: Long,
        totalSpentTime: Long,
        progress: Float,
        hasRunningTimer: Boolean
) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
                Column(
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text(
                                text = "Сегодня",
                                style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Box(
                                modifier = Modifier.size(200.dp),
                                contentAlignment = Alignment.Center
                        ) {
                                Box(
                                        modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                        color = MaterialTheme.colorScheme.surface,
                                                        shape = CircleShape
                                                )
                                )

                                CircularProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier.fillMaxSize(),
                                        color = if (hasRunningTimer)
                                                MaterialTheme.colorScheme.primary
                                        else
                                                MaterialTheme.colorScheme.tertiary,
                                        strokeWidth = 14.dp,
                                        trackColor = Color.Transparent,
                                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                                )

                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        if (hasRunningTimer) {
                                                Text("🦊", fontSize = 32.sp)
                                        } else {
                                                Text("🦔", fontSize = 32.sp)
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                                text = formatMillis(totalSpentTime),
                                                style = MaterialTheme.typography.headlineMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 28.sp,
                                                        color = if (hasRunningTimer)
                                                                MaterialTheme.colorScheme.primary
                                                        else
                                                                MaterialTheme.colorScheme.tertiary
                                                )
                                        )

                                        Text(
                                                text = "из ${formatMillis(totalGoalTime)}",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                                text = "${(progress * 100).toInt()}% выполнено",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        )

                        if (hasRunningTimer) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Box(
                                                modifier = Modifier
                                                        .size(8.dp)
                                                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                                text = "Таймер активен",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.Medium
                                                )
                                        )
                                }
                        }
                }
        }
}

@Composable
private fun CategoryCard(
        category: TimerCategory,
        timers: List<TimerEntity>,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        accentColor: Color
) {
        val totalTime = timers.sumOf { it.initialDurationMillis }
        val spentTime = timers.sumOf { it.initialDurationMillis - it.remainingTimeMillis }
        val progress = if (totalTime > 0) {
                (spentTime.toFloat() / totalTime.toFloat()).coerceIn(0f, 1f)
        } else 0f
        val hasRunning = timers.any { it.isRunning }

        Card(
                modifier = modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onClick() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
                Column(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                ) {
                        Box(
                                modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                                color = accentColor.copy(alpha = 0.15f),
                                                shape = CircleShape
                                        ),
                                contentAlignment = Alignment.Center
                        ) {
                                icon()
                        }

                        Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                ),
                                textAlign = TextAlign.Center
                        )

                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Text(
                                        text = "${timers.size} таймеров",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Box(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(accentColor.copy(alpha = 0.2f))
                                ) {
                                        Box(
                                                modifier = Modifier
                                                        .fillMaxWidth(progress)
                                                        .height(4.dp)
                                                        .clip(RoundedCornerShape(2.dp))
                                                        .background(if (hasRunning) accentColor else accentColor.copy(alpha = 0.6f))
                                        )
                                }

                                if (hasRunning) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                Box(
                                                        modifier = Modifier
                                                                .size(6.dp)
                                                                .background(accentColor, CircleShape)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                        text = "Активно",
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                                color = accentColor,
                                                                fontWeight = FontWeight.Medium
                                                        )
                                                )
                                        }
                                }
                        }
                }
        }
}
