package com.mikicorp.step

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class CreateTestUI(val context: Context?, private var docText: String) {
    private var myTest = arrayListOf<MyTest>()
    private val docList = arrayListOf<DocToList>()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onClose: () -> Unit = {}
    ) {
        docText.lines().map {str ->
            docList.add(DocToList(str, false))
        }
        DocToTest()
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
//                    ShowQuestion(activeQuestion)
//                    Spacer(modifier = Modifier.height(20.dp))
//                    HorizontalDivider()
//                    LazyColumn(state = listState) {
//                        items(items = activeQuestion.answers) { answer ->
//                            Spacer(modifier = Modifier.height(20.dp))
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                modifier = Modifier
//                                    .border(
//                                        if (answer.id == selectedAnswer) 2.dp else 0.dp,
//                                        if (answer.id == selectedAnswer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
//                                    )
//                                    .selectable(
//                                        selected = answer.id == selectedAnswer,
//                                        onClick = {
//                                            selectedAnswer = if (selectedAnswer != answer.id)
//                                                answer.id else -1
//                                        })
//                                    .padding(5.dp, 10.dp)
//                                    .fillMaxWidth()
//                            ) {
//                                Spacer(modifier = Modifier.width(15.dp))
//                                Icon(
//                                    imageVector = if (answer.id == selectedAnswer)
//                                        Icons.Filled.RadioButtonChecked
//                                    else
//                                        Icons.Filled.RadioButtonUnchecked,
//                                    tint = MaterialTheme.colorScheme.primary,
//                                    contentDescription = null
//                                )
//                                Spacer(modifier = Modifier.width(15.dp))
//
//                                Text(
//                                    text = answer.answer,
//                                    modifier = Modifier.fillMaxWidth(),
//                                )
//                            }
//                        }
//                    }
                    HorizontalDivider()
                    LazyColumn {
                        items(docList.size) {index ->
                            Row {
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Filled.QuestionAnswer,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    imageVector = if (docList[index].enabled)
                                        Icons.Filled.Add
                                    else
                                        Icons.Filled.Cancel,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(15.dp))
                                Text(
                                    text = docList[index].str,
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

    private fun DocToTest() {
        var i = 0
        while(i < docList.size) {
            val str = docList[i].str
            val question = str
            val answers = arrayListOf<MyAnswer>()
//            while(str[0] == 'a') {
//
//            }
            i++
        }
    }


}

data class DocToList(
    val str: String,
    val isQuestion: Boolean = false,
    val isAnswer: Boolean = false,
    val enabled: Boolean = false
)

data class MyTest(
    val question: String,
    val image: String = "",
    val answers: ArrayList<MyAnswer>,
)

data class MyAnswer(
    val answer: String,
    val image: String = "",
    val correct: Boolean = false
)

@Preview
@Composable
fun CreateTestUIPreview() {
    CreateTestUI(null, "").UI()
}