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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
        onSend: (
            categoryIndex: Int,
            subcategoryIndex: Int,
            themeIndex: Int,
            uri: Uri) -> Unit = {_,_,_,_ ->}
    ) {
        var mCategoryExpanded by remember { mutableStateOf(false) }
        var mSelectedCategory by remember { mutableIntStateOf(MainActivity.activeCategory) }
        var mCategoryFieldSize by remember { mutableStateOf(Size.Zero)}
        val categoryIcon = if (mCategoryExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        var mSubcategoryExpanded by remember { mutableStateOf(false) }
        var mSelectedSubcategory by remember { mutableIntStateOf(MainActivity.activeSubcategory) }
        var mSubcategoryFieldSize by remember { mutableStateOf(Size.Zero)}
        val subcategoryIcon = if (mSubcategoryExpanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        var mThemeExpanded by remember { mutableStateOf(false) }
        var mSelectedTheme by remember { mutableIntStateOf(MainActivity.activeSubcategory) }
        var mThemeFieldSize by remember { mutableStateOf(Size.Zero)}
        val themeIcon = if (mThemeExpanded)
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
                val fileName = context?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndexOrThrow("_display_name")
                    cursor.moveToFirst()
                    cursor.getString(nameIndex)
                } ?: "uploaded_file"
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        OutlinedTextField(
                            value = MainActivity.category[mSelectedCategory].name,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    mCategoryFieldSize = coordinates.size.toSize()
                                },
                            trailingIcon = {
                                Icon(
                                    modifier = Modifier
                                        .clickable {
                                            mCategoryExpanded = !mCategoryExpanded
                                        },
                                    imageVector = categoryIcon,
                                    contentDescription = ""
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = mCategoryExpanded,
                            onDismissRequest = { mCategoryExpanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { mCategoryFieldSize.width.toDp() })
                        ) {
                            MainActivity.category.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = item.name)
                                    },
                                    onClick = {
                                        mSelectedCategory = index
                                        mCategoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        OutlinedTextField(
                            value = MainActivity.category[mSelectedCategory].subCategory[mSelectedSubcategory].name,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    // This value is used to assign to
                                    // the DropDown the same width
                                    mSubcategoryFieldSize = coordinates.size.toSize()
                                },
                            trailingIcon = {
                                Icon(subcategoryIcon, "",
                                    Modifier.clickable {
                                        mSubcategoryExpanded = !mSubcategoryExpanded
                                    })
                            }
                        )

                        DropdownMenu(
                            expanded = mSubcategoryExpanded,
                            onDismissRequest = { mSubcategoryExpanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { mSubcategoryFieldSize.width.toDp() })
                        ) {
                            MainActivity.category[mSelectedCategory].subCategory.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = item.name)
                                    },
                                    onClick = {
                                        mSelectedSubcategory = index
                                        mSubcategoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        OutlinedTextField(
                            value = MainActivity.category[mSelectedCategory].subCategory[mSelectedSubcategory].themes[mSelectedTheme].name,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    mThemeFieldSize = coordinates.size.toSize()
                                },
                            trailingIcon = {
                                Icon(themeIcon, "",
                                    Modifier.clickable { mThemeExpanded = !mThemeExpanded })
                            }
                        )
                        DropdownMenu(
                            expanded = mThemeExpanded,
                            onDismissRequest = { mThemeExpanded = false },
                            modifier = Modifier
                                .width(with(LocalDensity.current) { mThemeFieldSize.width.toDp() })
                        ) {
                            MainActivity.category[mSelectedCategory].subCategory[mSelectedSubcategory].themes.forEachIndexed { index, item ->
                                DropdownMenuItem(
                                    text = {
                                        Text(text = item.name)
                                    },
                                    onClick = {
                                        mSelectedTheme = index
                                        mThemeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = fileName,
                        enabled = false,
                        onValueChange = {}
                    )

                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    actions = {
                        Button(
                            onClick = {
                                onSend(mSelectedCategory, mSelectedSubcategory, mSelectedTheme, uri)
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