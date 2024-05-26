package com.miki.step

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
        var isEnabled by remember {
            mutableStateOf(false)
        }
        val totalTime = 300
        var testTimer by remember { mutableIntStateOf(totalTime) }
        val radioOptions = listOf("A", "B", "C")
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }
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
                    actions = {
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .weight(0.5f, true)
                                .padding(15.dp, 0.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (isEnabled) Color.Red else Color.Gray,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_prev),
                                contentDescription = null
                            )
                        }
                        Button(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .weight(0.5f, true)
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
                                painter = painterResource(id = R.drawable.ic_next),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        )
    }

    private fun showTimer(timer: Int): String {
        return "${(timer / 3600).toInt()}:${((timer % 3600) / 60).toInt().toString().padStart(2, '0')}:${(timer % 60).toInt().toString().padStart(2, '0')}"
    }
}
