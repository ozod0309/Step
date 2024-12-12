package com.mikicorp.step

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

class SendMyTestUI(val context: Context?) {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        uri: Uri,
        onBackPressed: () -> Unit = {},
        onSend: (uri: Uri) -> Unit = {}
    ) {
        var mCategoryExpanded by remember { mutableStateOf(false) }
        var mSelectedText by remember { mutableStateOf("") }
        var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
        val icon = if (mCategoryExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.send_my_test),
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
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
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = mSelectedText,
                        onValueChange = { mSelectedText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                // This value is used to assign to
                                // the DropDown the same width
                                mTextFieldSize = coordinates.size.toSize()
                            },
                        label = {Text("Label")},
                        trailingIcon = {
                            Icon(icon,"contentDescription",
                                Modifier.clickable { mCategoryExpanded = !mCategoryExpanded })
                        }
                    )

                    DropdownMenu(
                        expanded = mCategoryExpanded,
                        onDismissRequest = { mCategoryExpanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                    ) {
                        MainActivity.category.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = item.name)
                                },
                                onClick = {
                                    mSelectedText = item.name
                                    mCategoryExpanded = false
                                }
                            )
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
                                onSend(uri)
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
                                text = stringResource(id = R.string.send),
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
fun SendMyTestPreview() {
    SendMyTestUI(null).UI(Uri.EMPTY)
}