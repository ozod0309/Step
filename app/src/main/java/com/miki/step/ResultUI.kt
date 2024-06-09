package com.miki.step

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Cancel
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
        val testQuestionsCount = MainActivity.tests.size
        val correctAnswerCount = MainActivity.tests.count { it.isCorrect && it.answered > 0 }
        val incorrectAnswerCount = MainActivity.tests.count { !it.isCorrect && it.answered > 0}
        val unAnswered = MainActivity.tests.count { !it.isCorrect && it.answered  == 0}
        val progress = correctAnswerCount.toFloat()/testQuestionsCount * 100
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
                            dataUsage = progress, name = "Test"
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider()
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.questions_count)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = testQuestionsCount.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Spacer(modifier = Modifier.width(20.dp))
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            tint = Color.Green,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.correct_answered)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = correctAnswerCount.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.incorrect_answered)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = incorrectAnswerCount.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.unanswered_questions)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = unAnswered.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }

                    HorizontalDivider()
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.total_time)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = 300.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
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
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            text = stringResource(id = R.string.spent_time)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            text = 200.toString()
                        )
                        Spacer(modifier = Modifier.width(20.dp))
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
}
