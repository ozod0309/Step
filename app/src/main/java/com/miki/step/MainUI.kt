package com.miki.step

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

class MainUI(context: Context) {
    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onSettings: () -> Unit,
        onInviteFriends: () -> Unit,
        onShare: () -> Unit,
        onCoders: () -> Unit,
        onLogout: () -> Unit
    ) {
        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(200.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary),
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp)
                        ) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Image(
                                alignment = Alignment.Center,
                                painter = rememberAsyncImagePainter(MainActivity.stepUser.pictureURL),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(4.dp, Color.Green),
                                        CircleShape
                                    )
                                    .padding(4.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                MainActivity.stepUser.getFullName(),
                                fontSize = 14.sp,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    lineHeight = 18.sp
                                ),
                                modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                            )
                            Text(
                                MainActivity.stepUser.accountName,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onTertiary,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    lineHeight = 14.sp
                                )
                            )
                        }
                    }
                    HorizontalDivider()
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.Settings,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        label = { Text(text = "Settings") },
                        selected = false,
                        onClick = { onSettings() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.PersonAdd,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        label = { Text(text = "Invite Friends") },
                        selected = false,
                        onClick = { onInviteFriends() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.Share,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        label = { Text(text = "Share") },
                        selected = false,
                        onClick = { onShare() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.Code,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        label = { Text(text = "Coders") },
                        selected = false,
                        onClick = { onCoders() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                tint = Color.Gray,
                                contentDescription = ""
                            )
                        },
                        label = { Text(text = "Logout") },
                        selected = false,
                        onClick = { onLogout() }
                    )
                }
            }
        ) {
            // Screen content
            val scrollBehavior =
                TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            var testTypesToggle by remember { mutableStateOf(false) }
            var testTypesHeight by remember { mutableIntStateOf(0) }
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.secondary,
                            scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text(
                                text = "Student Exam Preparation",
                                color = MaterialTheme.colorScheme.secondary,
                                maxLines = 1,
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        testTypesToggle = !testTypesToggle
                                        testTypesHeight = if (testTypesToggle) 150 else 0
                                    })
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Localized description",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                /* do something */
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Localized description",
                                    tint = MaterialTheme.colorScheme.secondary
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
                            .animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 2000,
                                    easing = LinearOutSlowInEasing
                                )
                            )
                            .background(MaterialTheme.colorScheme.secondary)
                            .clip(shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp))
                            .border(2.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(1) {
                                Spacer(modifier = Modifier.width(20.dp))
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .height(20.dp)
                                    )
                                    OutlinedButton(
                                        onClick = { },
                                        border = BorderStroke(
                                            3.dp,
                                            MaterialTheme.colorScheme.primary
                                        ),
                                        shape = RoundedCornerShape(15), // = 50% percent
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = MaterialTheme.colorScheme.secondary,
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                alignment = Alignment.Center,
                                                painter = rememberAsyncImagePainter(MainActivity.stepUser.pictureURL),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(64.dp)
                                            )
                                            Text(
                                                text = "DTM",
                                                fontSize = 14.sp,
                                                modifier = Modifier
                                                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )


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

                        )
                },
                floatingActionButton = {
                    Box {
                        FloatingActionButton(
                            onClick = { /* stub */ },
                            shape = CircleShape,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(80.dp)
                                .offset(y = 50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(45.dp)
                            )
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,

                )
        }
    }
}
