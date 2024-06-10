package com.miki.step

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.miki.step.lib.CircularProgressbar

class ResultUI(val context: Context?) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(onDone: () -> Unit) {
        val animDuration = 5000
        val testQuestionsCount = MainActivity.tests.size
        val correctAnswerCount = MainActivity.tests.count { it.isCorrect && it.answered > 0 }
        val incorrectAnswerCount = MainActivity.tests.count { !it.isCorrect && it.answered > 0}
        val unAnswered = MainActivity.tests.count { !it.isCorrect && it.answered  == 0}
        val progress = correctAnswerCount.toFloat()/testQuestionsCount * 100
        var anim by remember {
            mutableStateOf(false)
        }
        val animTestQuestionsCount by animateIntAsState(
            targetValue = if (anim) testQuestionsCount else 0,
            animationSpec = tween(
                durationMillis = animDuration,
                easing = LinearEasing
            ),
            label = ""
        )
        val animCorrectAnswerCount by animateIntAsState(
            targetValue = if (anim) correctAnswerCount else 0,
            animationSpec = tween(
                durationMillis = animDuration,
                easing = LinearEasing
            ),
            label = ""
        )
        val animIncorrectAnswerCount by animateIntAsState(
            targetValue = if (anim) incorrectAnswerCount else 0,
            animationSpec = tween(
                durationMillis = animDuration,
                easing = LinearEasing
            ),
            label = ""
        )
        val animUnAnswered by animateIntAsState(
            targetValue = if (anim) unAnswered else 0,
            animationSpec = tween(
                durationMillis = animDuration,
                easing = LinearEasing
            ),
            label = ""
        )
        LaunchedEffect(Unit) {
            anim = true
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.result),
                            textAlign = TextAlign.Center
                            )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressbar(
                            dataUsage = progress,
                            name = "Test",
                            animationDuration = animDuration
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        text = MainActivity.category[MainActivity.activeCategory].name
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.Quiz,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = stringResource(id = R.string.questions_count)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = animTestQuestionsCount.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = stringResource(id = R.string.correct_answered)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = animCorrectAnswerCount.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.Cancel,
                            tint = Color.Red,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = stringResource(id = R.string.incorrect_answered)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = animIncorrectAnswerCount.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.Circle,
                            tint = Color.Gray,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = stringResource(id = R.string.unanswered_questions)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = animUnAnswered.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.AccessTime,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = stringResource(id = R.string.total_time)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = showTimer(300)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.AccessTimeFilled,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            modifier = Modifier
                                .weight(1f),
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = stringResource(id = R.string.spent_time)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onTertiary,
                            text = showTimer(200)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        Box(
                            modifier = Modifier
                                .border(1.dp, MaterialTheme.colorScheme.primary)
                                .fillMaxWidth()
                                .wrapContentHeight()
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
                                                    .height(50.dp),
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
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Button(
                            onClick = {
                                onDone()
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
                            Text(
                                text = stringResource(id = R.string.close),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        )
    }
    private fun showTimer(timer: Int): String {
        return "${(timer / 3600)}:${
            ((timer % 3600) / 60).toString().padStart(2, '0')
        }:${(timer % 60).toString().padStart(2, '0')}"
    }

}
