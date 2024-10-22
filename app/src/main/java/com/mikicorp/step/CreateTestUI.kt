package com.mikicorp.step

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class CreateTestUI(val context: Context?, private var docText: String) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onClose: () -> Unit = {}
    ) {
        val docList = docText.lines()
        val scroll = rememberScrollState(0)
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.create_test),
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
                    ShowQuestion(activeQuestion)
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
                    HorizontalDivider()
                    Text(
                        modifier = Modifier.verticalScroll(scroll),
                        text = docText
                    )
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Button(
                            onClick = {
                                onClose()
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

@Preview(backgroundColor = 0xFFBBBBBB)
@Composable
fun ShowPreview() {
    CreateTestUI(null, "").UI()
}