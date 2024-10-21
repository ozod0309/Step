package com.mikicorp.step.msdocs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.mikicorp.step.MainActivity
import com.mikicorp.step.R
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream

class MSDocsUI(val context: Context?) {

    companion object {
        var docText = ""
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onClose: () -> Unit
    ) {
        openFileSelector()
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
                    Text(text = docText)
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

    fun openFileSelector() {
        openSpecificFolder("Documents")
    }

    private fun openSpecificFolder(folderName: String) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"  // Set the MIME type to filter files
            val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:$folderName")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        }
        MainActivity.requestFileLauncher.launch(intent)
    }

    @SuppressLint("Range")
    fun handleSelectedFile(uri: Uri) {
        val cursor = context?.contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                displayName?.let {file ->
                    val retrievedDoc = File(file)
                    retrievedDoc.let {doc ->
                        docText = try {
                            //Reading it as stream
                            val docStream = FileInputStream(doc)
                            val targetDoc = XWPFDocument(docStream)

                            //creating a constructor object for extracting text from the word document
                            val wordExtractor = XWPFWordExtractor(targetDoc)
                            wordExtractor.text
                        } catch (e: Exception) {
                            e.printStackTrace().toString()
                        }
//                        UI(
//                            onClose = {
//
//                            }
//                        )
                    }
                }
            }
        }
    }
}