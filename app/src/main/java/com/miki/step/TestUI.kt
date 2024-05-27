package com.miki.step

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.RadioButton
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class TestUI(val context: Context?) {
    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onBackPressed: () -> Unit,
        onTimeOut: () -> Unit

    ) {
        val isEnabled by remember {
            mutableStateOf(false)
        }
        val totalTime = 300
        var testTimer by remember { mutableIntStateOf(totalTime) }
        val showTestResult = remember {
            mutableStateOf(false)
        }
        var bottomHeight by remember { mutableIntStateOf(0) }
        val radioOptions = listOf("A", "B", "C")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
        LaunchedEffect(Unit) {
            while (true) {
                delay(1.seconds)
                testTimer--
                if (testTimer == 0) {
                    onTimeOut()
                }
            }
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Top App Bar")
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onBackPressed()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = ""
                            )
                        }
                    },
                    actions = {
                        Text(
                            text = showTimer(testTimer),
                            color = if (testTimer > totalTime * 0.1) MaterialTheme.colorScheme.secondary else Color.Red
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            content = { innerPadding ->
                bottomHeight = innerPadding.calculateBottomPadding().value.toInt()
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Test Test Test Test"
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn {
                        items(radioOptions) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (it == selectedOption),
                                        onClick = {
                                            onOptionSelected(it)
                                        }
                                    )
                                    .padding(horizontal = 16.dp)
                            ) {
                                RadioButton(
                                    selected = (it == selectedOption),
                                    onClick = { onOptionSelected(it) }
                                )
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium.merge(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isEnabled) Color.Red else Color.Gray,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ChevronLeft,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = null
                            )
                        }
                        FloatingActionButton(
                            onClick = {
                                showTestResult.value = !showTestResult.value
                            },
//                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .size(65.dp)
                                .clip(CircleShape)
//                                .offset(y = 50.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Dataset,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(45.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isEnabled) Color.Red else Color.Gray,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        )
        if (showTestResult.value) {
            OpenTestResults(
                testResultShow = showTestResult,
                bottomHeight = bottomHeight
            )
        }
    }

    @Composable
    fun OpenTestResults(testResultShow: MutableState<Boolean>, bottomHeight: Int) {
        var anim by remember {
            mutableStateOf(false)
        }
        val animateAlpha by animateFloatAsState(
            targetValue = if (anim) 0.5f else 0f,
            animationSpec = tween(
                durationMillis = 0,
                easing = LinearEasing
            ),
            label = ""
        )
        val animatedBoxHeight by animateDpAsState(
            targetValue = if (anim) 150.dp else 0.dp,
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            ),
            label = ""
        )
        LaunchedEffect(Unit) {
            anim = true
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { testResultShow.value = !testResultShow.value })
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        bottom = bottomHeight.dp
                    )
                    .fillMaxSize()
                    .graphicsLayer { alpha = animateAlpha }
                    .background(Color.Black)
            )
        }
        Column {
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .padding(bottom = bottomHeight.dp)
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .border(2.dp, MaterialTheme.colorScheme.primary)
                    .background(MaterialTheme.colorScheme.secondary)
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                val numbers = (0..20).toList()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                ) {
                    items(
                        numbers.size,
                        key = {
                            it
                        }
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Filled.CheckBox, contentDescription = "")
                            Text(text = "  $it")
                        }
                    }
                }
            }
        }
    }

    private fun showTimer(timer: Int): String {
        return "${(timer / 3600)}:${
            ((timer % 3600) / 60).toString().padStart(2, '0')
        }:${(timer % 60).toString().padStart(2, '0')}"
    }
}
