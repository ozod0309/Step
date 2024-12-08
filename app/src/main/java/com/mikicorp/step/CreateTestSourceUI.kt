package com.mikicorp.step

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

class CreateTestSourceUI(val context: Context?) {
    companion object {
        var testText: String = ""
    }
    @OptIn(ExperimentalMaterial3Api::class)

    @Composable
    fun UI(
        onMSDocs: () -> Unit = {},
        onPDFDocs: () -> Unit = {},
        onOCR: () -> Unit = {},
        onGPTParse: () -> Unit = {},
        onClose: () -> Unit = {}
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.create_test_source),
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
                    Button(
                        onClick = {
                            onMSDocs()
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
                            text = stringResource(id = R.string.ms_office_docs),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onPDFDocs()
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
                            text = stringResource(id = R.string.pdf_docs),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onOCR()
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
                            text = stringResource(id = R.string.ocr),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Button(
                        onClick = {
                            onGPTParse()
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
                            text = stringResource(id = R.string.ai_ocr),
                            color = MaterialTheme.colorScheme.primary
                        )
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
}

@Preview
@Composable
fun CreateTestSourceUIPreview() {
    CreateTestSourceUI(null).UI()
}