package com.miki.step

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

class MainUI(context: Context) {
    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI() {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Drawer title", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "Drawer Item") },
                        selected = false,
                        onClick = { /*TODO*/ }
                    )
                    // ...other drawer items
                }
            }
        ) {
            // Screen content
            val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                rememberTopAppBarState()
            )
            var testTypesToggle by remember {
                mutableStateOf(false)
            }
            var testTypesHeight by remember {
                mutableIntStateOf(0)
            }
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                        ),
                        title = {
                            Text(
                                text = "Student Exam Preparation",
                                color = MaterialTheme.colorScheme.onTertiary,
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                    testTypesToggle = !testTypesToggle
                                    testTypesHeight = if(testTypesToggle) 100 else 0
                                })
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                /* do something */
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                /* do something */
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                content = { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .height(testTypesHeight.dp)
                            .fillMaxWidth()
                            .animateContentSize()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            items(1) {
                                Text(text = "DTM")
                                Text(text = "PDD")
                            }
                        }
                    }
                    Box(modifier = Modifier.padding(innerPadding)) {
                        LazyColumn {
                            items((1..100).toList()) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Item $it"
                                )
                            }
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        actions = {
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Localized description"
                                )
                            }
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Localized description",
                                )
                            }
                            IconButton(onClick = { /* do something */ }) {
                                Icon(
                                    Icons.Filled.ShoppingCart,
                                    contentDescription = "Localized description",
                                )
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = { /* do something */ },
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            ) {
                                Icon(Icons.Filled.Add, "Localized description")
                            }
                        }
                    )
                }
            )
        }
    }
}
