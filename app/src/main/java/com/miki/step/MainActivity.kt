package com.miki.step

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.miki.step.ui.theme.StepTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StepTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModalNavigationDrawer(
                        drawerContent = {
                            ModalDrawerSheet {
                                Text("Drawer title", modifier = Modifier.padding(16.dp))
                                Divider()
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
                        var heightTestType by remember {
                            mutableStateOf(0)
                        }
                        Scaffold(
                            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                            topBar = {
                                LargeTopAppBar(
                                    colors = TopAppBarDefaults.largeTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        titleContentColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    title = {
                                        Text(
                                            text = "STEP",
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.clickable(onClick = {
                                                testTypesToggle = !testTypesToggle
                                                heightTestType = if(testTypesToggle) 100 else 0
                                            })
                                        )
                                    },
                                    navigationIcon = {
                                        IconButton(onClick = { /* do something */ }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = "Localized description"
                                            )
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = { /* do something */ }) {
                                            Icon(
                                                imageVector = Icons.Filled.Menu,
                                                contentDescription = "Localized description"
                                            )
                                        }
                                    },
                                    scrollBehavior = scrollBehavior
                                )
                            },
                            content = { innerPadding ->
                                Box(
                                    modifier = Modifier.padding(innerPadding).height(heightTestType.dp),

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
                                                Icons.Filled.Send,
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
        }
    }
}