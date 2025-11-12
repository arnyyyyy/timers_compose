package com.arno.timers_compose.feature_timer_detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arno.timers_compose.core.AppViewModelProvider
import com.arno.timers_compose.feature_timers_list.TimerViewModel
import com.arno.timers_compose.utils.TimeFormatter.formatMillis
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerDetailScreen(
        timerId: String,
        onNavigateBack: () -> Unit,
        onNavigateToEdit: (String) -> Unit,
        viewModel: TimerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
        val timersState = viewModel.timers.collectAsState()
        val timer = timersState.value.find { it.id == timerId }

        LaunchedEffect(Unit) {
                viewModel.refreshTimers()
        }

        if (timer == null) {
                Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                ) {
                        CircularProgressIndicator()
                }
                return
        }

        val progress =
                1f - (timer.remainingTimeMillis.toFloat() / timer.initialDurationMillis.toFloat())
        val animatedProgress by animateFloatAsState(
                targetValue = progress,
                animationSpec = tween(300),
                label = "progress"
        )

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = {
                                        Text(
                                                text = "Детали таймера",
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                )
                                        )
                                },
                                navigationIcon = {
                                        IconButton(onClick = onNavigateBack) {
                                                Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                        contentDescription = "Назад"
                                                )
                                        }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                        )
                }
        ) { padding ->
                Column(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .verticalScroll(rememberScrollState())
                                .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                                text = timer.name,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                ),
                                textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Box(
                                modifier = Modifier.size(280.dp),
                                contentAlignment = Alignment.Center
                        ) {
                                Box(
                                        modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                                        shape = CircleShape
                                                )
                                )

                                CircularProgressIndicator(
                                        progress = { animatedProgress },
                                        modifier = Modifier.fillMaxSize(),
                                        color = if (timer.isRunning)
                                                MaterialTheme.colorScheme.primary
                                        else
                                                MaterialTheme.colorScheme.tertiary,
                                        strokeWidth = 16.dp,
                                        trackColor = Color.Transparent,
                                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                                )

                                Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Text(
                                                text = formatMillis(timer.remainingTimeMillis),
                                                style = MaterialTheme.typography.displayLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 52.sp,
                                                        color = if (timer.isRunning)
                                                                MaterialTheme.colorScheme.primary
                                                        else
                                                                MaterialTheme.colorScheme.tertiary,
                                                        letterSpacing = (-1).sp
                                                )
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                                text = "${(progress * 100).toInt()}% завершено",
                                                style = MaterialTheme.typography.bodyLarge.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontWeight = FontWeight.Medium
                                                )
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(
                                        16.dp,
                                        Alignment.CenterHorizontally
                                )
                        ) {
                                Button(
                                        onClick = { viewModel.resetTimer(timer) },
                                        modifier = Modifier.size(64.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        ),
                                        contentPadding = PaddingValues(0.dp)
                                ) {
                                        Icon(
                                                imageVector = Icons.Filled.RestartAlt,
                                                contentDescription = "Сброс прогресса",
                                                modifier = Modifier
                                                        .size(32.dp)
                                                        .graphicsLayer(
                                                                scaleX = 1.08f,
                                                                scaleY = 1.08f
                                                        )
                                        )
                                }

                                Button(
                                        onClick = { viewModel.toggleTimer(timer.id) },
                                        modifier = Modifier.size(80.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                                containerColor = if (timer.isRunning)
                                                        MaterialTheme.colorScheme.tertiary
                                                else
                                                        MaterialTheme.colorScheme.primary
                                        ),
                                        contentPadding = PaddingValues(0.dp),
                                        elevation = ButtonDefaults.buttonElevation(
                                                defaultElevation = 4.dp,
                                                pressedElevation = 2.dp
                                        )
                                ) {
                                        Icon(
                                                imageVector = if (timer.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                                contentDescription = if (timer.isRunning) "Пауза" else "Старт",
                                                modifier = Modifier.size(40.dp)
                                        )
                                }

                                Button(
                                        onClick = { onNavigateToEdit(timer.id) },
                                        modifier = Modifier.size(64.dp),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                        ),
                                        contentPadding = PaddingValues(0.dp)
                                ) {
                                        Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Редактировать",
                                                modifier = Modifier.size(28.dp)
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(40.dp))

                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                                Column(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(20.dp)
                                ) {
                                        Text(
                                                text = "Информация",
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                )
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        InfoRow(
                                                label = "Статус",
                                                value = when {
                                                        timer.isRunning -> "В процессе"
                                                        timer.isPaused -> "На паузе"
                                                        else -> "Остановлен"
                                                },
                                                valueColor = when {
                                                        timer.isRunning -> MaterialTheme.colorScheme.primary
                                                        timer.isPaused -> MaterialTheme.colorScheme.tertiary
                                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                }
                                        )

                                        HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                thickness = DividerDefaults.Thickness,
                                                color = DividerDefaults.color
                                        )

                                        InfoRow(
                                                label = "Общая длительность",
                                                value = formatMillis(timer.initialDurationMillis)
                                        )

                                        HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                thickness = DividerDefaults.Thickness,
                                                color = DividerDefaults.color
                                        )

                                        InfoRow(
                                                label = "Осталось",
                                                value = formatMillis(timer.remainingTimeMillis)
                                        )

                                        if (timer.isRunning && timer.lastStartedTime > 0) {
                                                HorizontalDivider(
                                                        modifier = Modifier.padding(vertical = 12.dp),
                                                        thickness = DividerDefaults.Thickness,
                                                        color = DividerDefaults.color
                                                )

                                                InfoRow(
                                                        label = "Начат в",
                                                        value = SimpleDateFormat(
                                                                "HH:mm:ss",
                                                                Locale.getDefault()
                                                        )
                                                                .format(Date(timer.lastStartedTime))
                                                )
                                        }

                                        HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 12.dp),
                                                thickness = DividerDefaults.Thickness,
                                                color = DividerDefaults.color
                                        )

                                        InfoRow(
                                                label = "Тип",
                                                value = when (timer.timerType.name) {
                                                        "DAILY" -> "Ежедневный"
                                                        "WEEKLY" -> "Еженедельный"
                                                        "INFINITE" -> "Бесконечный"
                                                        else -> "Обычный"
                                                }
                                        )

                                        if (timer.selectedDays.isNotEmpty()) {
                                                HorizontalDivider(
                                                        modifier = Modifier.padding(vertical = 12.dp),
                                                        thickness = DividerDefaults.Thickness,
                                                        color = DividerDefaults.color
                                                )

                                                Column {
                                                        Text(
                                                                text = "Дни недели",
                                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                                )
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Row(
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                        8.dp
                                                                ),
                                                                modifier = Modifier.fillMaxWidth()
                                                        ) {
                                                                timer.selectedDays.forEach { day ->
                                                                        Surface(
                                                                                shape = RoundedCornerShape(
                                                                                        8.dp
                                                                                ),
                                                                                color = MaterialTheme.colorScheme.primaryContainer
                                                                        ) {
                                                                                Text(
                                                                                        text = day.take(
                                                                                                3
                                                                                        ),
                                                                                        modifier = Modifier.padding(
                                                                                                horizontal = 12.dp,
                                                                                                vertical = 6.dp
                                                                                        ),
                                                                                        style = MaterialTheme.typography.bodySmall.copy(
                                                                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                                                                fontWeight = FontWeight.Medium
                                                                                        )
                                                                                )
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                                onClick = {
                                        viewModel.deleteTimer(timer.id)
                                        onNavigateBack()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                )
                        ) {
                                Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Удалить таймер")
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                }
        }
}

@Composable
private fun InfoRow(
        label: String,
        value: String,
        valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
                Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                )
                Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = valueColor
                        )
                )
        }
}
