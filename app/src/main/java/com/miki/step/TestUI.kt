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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import com.miki.step.lib.EquationItem
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class TestUI(context: Context?) {
    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onBackPressed: () -> Unit,
        onTimeOut: () -> Unit,
        onFinishTest: () -> Unit
    ) {
        val totalTime = 300
        var testTimer by remember { mutableIntStateOf(totalTime) }
        var testProgressBarAlpha by remember { mutableIntStateOf(0) }
        val showTestResult = remember { mutableStateOf(false) }
        var bottomHeight by remember { mutableIntStateOf(0) }

        val listState = rememberLazyListState()
        var activeQuestionIndex by remember { mutableIntStateOf(0) }
        var activeQuestion by remember {
            mutableStateOf(MainActivity.tests[activeQuestionIndex])
        }
        var selectedAnswer by remember { mutableIntStateOf(-1) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1.seconds)
                testTimer--
                testProgressBarAlpha = (255 * (totalTime - testTimer) / totalTime)
                if (testTimer == 0) {
                    onTimeOut()
                }
            }
        }
        Scaffold(
            topBar = {
                Column {
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
                    Box(
                        modifier = Modifier
                            .height(25.dp)
                            .fillMaxWidth()
                            .background(
                                Color(
                                    red = 255,
                                    green = 0,
                                    blue = 0,
                                    alpha = testProgressBarAlpha
                                )
                            )
                    ) {
                        val answered = MainActivity.tests.count { it.answered != 0 }
                        val progressBarValue = answered.toFloat() / MainActivity.tests.size
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progressBarValue)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100))
                        ) {
                            Text(
                                text = answered.toString(),
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.padding(5.dp, 0.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = MainActivity.tests.count { it.answered == 0 }.toString(),
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.padding(5.dp, 0.dp)
                            )
                        }
                    }
                }
            },
            content = { innerPadding ->
                bottomHeight = innerPadding.calculateBottomPadding().value.toInt()
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Column {
                        EquationItem(
                            line = EquationItem(
                                line = EquationItem(
                                    line = "x",
                                    superscript = "2"
                                ),
                                underline = "y"
                            ),
                            sqrt = 2
                        ).Show()
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = activeQuestion.question
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    LazyColumn(state = listState) {
                        items(items = activeQuestion.answers) { answer ->
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .border(
                                        if (answer.id == selectedAnswer) 2.dp else 0.dp,
                                        if (answer.id == selectedAnswer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                    )
                                    .selectable(
                                        selected = answer.id == selectedAnswer,
                                        onClick = {
                                            selectedAnswer = if (selectedAnswer != answer.id)
                                                answer.id else -1
                                        })
                                    .padding(5.dp, 10.dp)
                                    .fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.width(15.dp))
                                Icon(
                                    imageVector = if (answer.id == selectedAnswer)
                                        Icons.Filled.RadioButtonChecked
                                    else
                                        Icons.Filled.RadioButtonUnchecked,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(15.dp))

                                Text(
                                    text = answer.answer,
                                    modifier = Modifier.fillMaxWidth(),
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
                            onClick = {
                                do {
                                    activeQuestionIndex--
                                    if (activeQuestionIndex < 0) activeQuestionIndex =
                                        MainActivity.tests.size - 1
                                } while (MainActivity.tests[activeQuestionIndex].answered > 0)
                                activeQuestion = MainActivity.tests[activeQuestionIndex]
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
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
                            onClick = {
                                if (selectedAnswer != -1) {
                                    MainActivity.tests[activeQuestionIndex].answered =
                                        selectedAnswer
                                    selectedAnswer = -1
                                }
                                if (MainActivity.tests.count { it.answered <= 0 } > 0) {
                                    do {
                                        activeQuestionIndex++
                                        if (activeQuestionIndex == MainActivity.tests.size) activeQuestionIndex =
                                            0
                                    } while (MainActivity.tests[activeQuestionIndex].answered > 0)
                                    activeQuestion = MainActivity.tests[activeQuestionIndex]
                                } else {
                                    onFinishTest()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(15.dp, 0.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = if (selectedAnswer >= 0)
                                    Icons.Filled.Check
                                else
                                    Icons.Filled.ChevronRight,
                                tint = MaterialTheme.colorScheme.primary,
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
                bottomHeight = bottomHeight,
                onChosen = { index ->
                    if (index >= 0 && index < MainActivity.tests.size) {
                        activeQuestionIndex = index
                        activeQuestion = MainActivity.tests[activeQuestionIndex]
                        selectedAnswer =
                            if (activeQuestion.answered > 0) activeQuestion.answered else -1
                    }
                }
            )
        }
    }

    @Composable
    fun OpenTestResults(
        testResultShow: MutableState<Boolean>,
        bottomHeight: Int,
        onChosen: (index: Int) -> Unit
    ) {
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
        val hh = if (Math.floorDiv(MainActivity.tests.size - 1, 10) < 5) (Math.floorDiv(
            MainActivity.tests.size - 1,
            10
        ) + 1) * 50 else 200
        val animatedBoxHeight by animateDpAsState(
            targetValue = if (anim) hh.dp else 0.dp,
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
                    .height(animatedBoxHeight)
                    .clickable { }
            ) {
                val numbers = (1..MainActivity.tests.size).toList()
                Column(
                    Modifier.verticalScroll(rememberScrollState())
                ) {
                    for (i in 0 until Math.floorDiv(numbers.size - 1, 10) + 1) {
                        Row {
                            repeat(10) { j ->
                                val index = i * 10 + j
                                Box(
                                    Modifier
                                        .weight(1f)
                                        .height(50.dp)
                                        .clickable {
                                            testResultShow.value = !testResultShow.value
                                            onChosen(index)
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (index < numbers.size) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                imageVector = if (MainActivity.tests[index].answered != 0)
                                                    Icons.Filled.CheckBox
                                                else
                                                    Icons.Filled.CheckBoxOutlineBlank,
                                                tint = MaterialTheme.colorScheme.primary,
                                                contentDescription = ""
                                            )
                                            Text(
                                                text = numbers[index].toString(),
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                        }
                                    }
                                }
                            }
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
