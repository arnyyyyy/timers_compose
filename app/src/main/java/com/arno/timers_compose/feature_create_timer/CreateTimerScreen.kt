package com.arno.timers_compose.feature_create_timer

import android.view.LayoutInflater
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import android.widget.TimePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.arno.timers_compose.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimerScreen(
        onCreateTimer: (timerData: CreateTimerData) -> Unit,
        onNavigateBack: () -> Unit
) {
        var timerData by remember { mutableStateOf(CreateTimerData()) }

        val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                        Color(0xFF88CCFF).copy(alpha = 0.7f),
                        Color(0xFFCCDDFF).copy(alpha = 0.6f)
                )
        )

        Scaffold(
                modifier = Modifier.background(gradientBrush),
                topBar = {
                        CenterAlignedTopAppBar(
                                title = { },
                                navigationIcon = {
                                        IconButton(onClick = onNavigateBack) {
                                                Icon(
                                                        Icons.AutoMirrored.Filled.ArrowBack,
                                                        stringResource(R.string.back)
                                                )
                                        }
                                },
                                actions = {
                                        IconButton(
                                                onClick = {
                                                        onCreateTimer(timerData)
                                                        onNavigateBack()
                                                }
                                        ) {
                                                Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = stringResource(R.string.create_timer)
                                                )
                                        }
                                },
                                colors = TopAppBarColors(
                                        containerColor = Color.Transparent,
                                        scrolledContainerColor = Color.Transparent,
                                        navigationIconContentColor = Color.White,
                                        titleContentColor = Color.Transparent,
                                        actionIconContentColor = Color.White,
                                ),
                        )
                },
                containerColor = Color.Transparent
        ) { innerPadding ->
                Box(
                        modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        contentAlignment = Alignment.Center
                ) {
                        Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier.fillMaxSize()
                        )
                        {
                                BasicTextField(
                                        value = timerData.name,
                                        onValueChange = { timerData.name = it },
                                        singleLine = true,
                                        modifier = Modifier
                                                .size(width = 220.dp, height = 60.dp)

                                ) { innerTextField ->
                                        OutlinedTextFieldDefaults.DecorationBox(
                                                value = timerData.name,
                                                innerTextField = innerTextField,
                                                enabled = true,
                                                singleLine = true,
                                                visualTransformation = VisualTransformation.None,
                                                interactionSource = remember { MutableInteractionSource() },
                                                placeholder = {
                                                        Text(
                                                                stringResource(R.string.timers_name),
                                                                color = Color.Gray,
                                                        )
                                                },
                                                container = {
                                                        OutlinedTextFieldDefaults.Container(
                                                                enabled = true,
                                                                isError = false,
                                                                interactionSource = remember { MutableInteractionSource() },
                                                                colors = OutlinedTextFieldDefaults.colors(
                                                                        focusedBorderColor = Color.Gray,
                                                                        unfocusedBorderColor = Color.Gray,
                                                                        focusedTextColor = Color.Gray,
                                                                        unfocusedTextColor = Color.Gray,
                                                                        disabledTextColor = Color.LightGray,
                                                                ),
                                                                shape = RoundedCornerShape(6.dp),
                                                                focusedBorderThickness = 2.5.dp,
                                                                unfocusedBorderThickness = 2.5.dp,
                                                        )
                                                }
                                        )
                                }
                                AndroidView(
                                        factory = { context ->
                                                val inflater =
                                                        LayoutInflater.from(context)
                                                val timePicker = inflater.inflate(
                                                        R.layout.time_picker_spinner,
                                                        null,
                                                        false
                                                ) as TimePicker

                                                timePicker.apply {
                                                        setIs24HourView(true)
                                                        setOnTimeChangedListener { _, hour, minute ->
                                                                timerData.minutes = minute
                                                                timerData.hours = hour
                                                        }
                                                        scaleX = 1.3f
                                                        scaleY = 1.3f
                                                }
                                        },
                                        modifier = Modifier
                                                .padding(bottom = 24.dp)
                                                .size(250.dp, 250.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                        }
                }
        }
}

@Preview(showBackground = true)
@Composable
fun CreateTimerScreenPreview() {
        CreateTimerScreen(onCreateTimer = { _ -> }, onNavigateBack = {})
}