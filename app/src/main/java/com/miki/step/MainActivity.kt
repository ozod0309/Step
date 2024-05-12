package com.miki.step

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
                    modifier = Modifier.fillMaxSize(),
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
                                            "STEP",
                                            maxLines = 1,
                                            textAlign = TextAlign.Center,
                                            overflow = TextOverflow.Ellipsis
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
                            bottomBar = {
                                BottomAppBar(
                                    actions = {
                                        IconButton(onClick = { /* do something */ }) {
                                            Icon(Icons.Filled.Check, contentDescription = "Localized description")
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
                        ) { innerPadding ->
                            val a=innerPadding
//                            ScrollContent(innerPadding)
                            LazyColumn {
                                items((1..1000).toList()) {
                                    Text(text = "Item $it")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        StepTheme {
            Greeting("Android")
        }
    }
}