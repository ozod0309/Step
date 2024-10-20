package com.mikicorp.step

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.URLUtil
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

class MainUI(val context: Context) {

    companion object {
        var categoryTop = 0.dp
    }

    data class BottomItem(
        val icon: ImageVector,
        val text: String
    )

    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI(
        onStartTest: (subCategoryId: Int) -> Unit,
        onProfile: () -> Unit,
        onSettings: () -> Unit,
        onInviteFriends: () -> Unit,
        onShare: () -> Unit,
        onRateUs: () -> Unit,
        onNotification: (message: String) -> Unit,
        onCreateTest: () -> Unit,
        onEditTest: () -> Unit,
        onDeleteTest: () -> Unit,
        onLogout: () -> Unit
    ) {
        val bottomBarData = arrayListOf(
            BottomItem(Icons.Filled.Home, context.resources.getString(R.string.home)),
            BottomItem(Icons.Filled.BarChart, context.resources.getString(R.string.success)),
            BottomItem(Icons.Filled.History, context.resources.getString(R.string.history)),
            BottomItem(Icons.AutoMirrored.Filled.ListAlt, context.resources.getString(R.string.my_step))
        )
        val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val categoryShow = remember {
            mutableStateOf(false)
        }
        val activeCategory = remember {
            mutableIntStateOf(MainActivity.activeCategory)
        }
        val bottomBarSelector = remember {
            mutableIntStateOf(MainActivity.bottomBarSelector)
        }
        val showFloatingButton = remember {
            mutableStateOf(true)
        }
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
                                Icons.Filled.Person,
                                tint = Color.Gray,
                                contentDescription = context.resources.getString(R.string.profile)
                            )
                        },
                        label = { Text(text = context.resources.getString(R.string.profile)) },
                        selected = false,
                        onClick = { onProfile() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.Settings,
                                tint = Color.Gray,
                                contentDescription = context.resources.getString(R.string.settings)
                            )
                        },
                        label = { Text(text = context.resources.getString(R.string.settings)) },
                        selected = false,
                        onClick = { onSettings() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.PersonAdd,
                                tint = Color.Gray,
                                contentDescription = context.resources.getString(R.string.invite_friends)
                            )
                        },
                        label = { Text(text = context.resources.getString(R.string.invite_friends)) },
                        selected = false,
                        onClick = { onInviteFriends() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.Share,
                                tint = Color.Gray,
                                contentDescription = context.resources.getString(R.string.share)
                            )
                        },
                        label = { Text(text = context.resources.getString(R.string.share)) },
                        selected = false,
                        onClick = { onShare() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Filled.Star,
                                tint = Color.Gray,
                                contentDescription = context.resources.getString(R.string.rate_us)
                            )
                        },
                        label = { Text(text = context.resources.getString(R.string.rate_us)) },
                        selected = false,
                        onClick = { onRateUs() }
                    )
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                tint = Color.Gray,
                                contentDescription = context.resources.getString(R.string.logout)
                            )
                        },
                        label = { Text(text = context.resources.getString(R.string.logout)) },
                        selected = false,
                        onClick = { onLogout() }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = context.resources.getString(R.string.created_by),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        ) {
            showFloatingButton.value =
                when (bottomBarSelector.intValue) {
                    3 -> false
                    else -> true
                }
            // Screen content
            val scrollBehavior =
                TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    when(bottomBarSelector.intValue) {
                        0 -> MainTop(
                            activeCategory = activeCategory,
                            categoryShow = categoryShow,
                            scrollBehavior = scrollBehavior,
                            onOpenDrawer = {
                                scope.launch { drawerState.open() }
                            }
                        )
                        1 -> SuccessTop()
                        2 -> HistoryTop()
                        3 -> MyStepTop()
                    }
                },
                content = { innerPadding ->
                    categoryTop = innerPadding.calculateTopPadding()
                    when(bottomBarSelector.intValue) {
                        0 -> MainContent(
                            activeCategory = activeCategory,
                            innerPadding = innerPadding,
                            onStartTest = {index ->
                                onStartTest(index)
                            }
                        )
                        1 -> SuccessContent()
                        2 -> HistoryContent()
                        3 -> MyStepContent(
                            innerPadding = innerPadding,
                            onCreateTest = {
                                onCreateTest()
                            },
                            onEditTest = {
                                onEditTest()
                            },
                            onDeleteTest = {
                                onDeleteTest()
                            }
                        )
                    }
                },
                bottomBar = {
                    BottomAppBar(
                        containerColor = MaterialTheme.colorScheme.primary,
                        actions = {
                            bottomBarData.forEachIndexed { index, item ->
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            bottomBarSelector.intValue = index
                                            MainActivity.bottomBarSelector = index
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        item.icon,
                                        tint = if (bottomBarSelector.intValue == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary,
                                        contentDescription = item.text
                                    )
                                    Text(
                                        text = item.text,
                                        color = if (bottomBarSelector.intValue == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary,
                                    )
                                    if (bottomBarSelector.intValue == index) {
                                        HorizontalDivider(
                                            thickness = 2.dp,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    )
                },
                floatingActionButton = {
                    if(showFloatingButton.value) {
                        Box {
                            FloatingActionButton(
                                onClick = {
                                    onNotification("New Turbo Test")
                                },
//                            shape = CircleShape,
                                containerColor = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(80.dp)
//                                .offset(y = 50.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(45.dp),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = context.resources.getString(R.string.fast_test),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.End
            )
        }
        if (categoryShow.value) {
            OpenCategory(categoryShow, activeCategory)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainTop(
        activeCategory: MutableIntState,
        categoryShow: MutableState<Boolean>,
        scrollBehavior: TopAppBarScrollBehavior,
        onOpenDrawer: () -> Unit
    ) {
        LargeTopAppBar(
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.secondary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    text = MainActivity.category[activeCategory.intValue].name,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {
                            categoryShow.value = !categoryShow.value
                        })
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    onOpenDrawer()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = context.resources.getString(R.string.menu),
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
                        contentDescription = context.resources.getString(R.string.notificaations),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    }

    @Composable
    private fun MainContent(
        activeCategory: MutableIntState,
        innerPadding: PaddingValues,
        onStartTest: (subCategoryId: Int) -> Unit
    ) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(MainActivity.category[activeCategory.intValue].subCategory.size) { index ->
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp, 10.dp)
                            .fillMaxWidth()
                            .clickable {
                                onStartTest(index)
                            }
                    ) {
                        Spacer(modifier = Modifier.width(15.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = MainActivity.category[activeCategory.intValue].subCategory[index].name,
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SuccessTop() {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.success),
                    textAlign = TextAlign.Center
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Composable
    private fun SuccessContent() {
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HistoryTop() {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.history),
                    textAlign = TextAlign.Center
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Composable
    private fun HistoryContent() {
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MyStepTop() {
        TopAppBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.my_step),
                    textAlign = TextAlign.Center
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.secondary,
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Composable
    private fun MyStepContent(
        innerPadding: PaddingValues,
        onCreateTest: () -> Unit,
        onEditTest: () -> Unit,
        onDeleteTest: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    .fillMaxSize()
                    .weight(1f)
            ) {
                LazyColumn {
//  List of My Tests
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        onCreateTest()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(15.dp, 0.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                }
                Button(
                    onClick = {
                        onEditTest()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(15.dp, 0.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                }
                Button(
                    onClick = {
                        onDeleteTest()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(15.dp, 0.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        tint = MaterialTheme.colorScheme.secondary,
                        contentDescription = null
                    )
                }
            }
        }

/*         Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.create_test),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onCreateTest()
                    }
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.edit_test),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onEditTest()
                    }
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.delete_test),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onDeleteTest()
                    }
            )
        }*/
    }


    @Composable
    fun OpenCategory(
        categoryShow: MutableState<Boolean>,
        activeCategory: MutableIntState
    ) {
        var anim by remember {
            mutableStateOf(false)
        }
        val animateAlpha by animateFloatAsState(
            targetValue = if (anim) 0.5f else 0f,
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            ),
            label = ""
        )
        val animatedBoxHeight by animateDpAsState(
            targetValue = if (anim) 150.dp else 0.dp,
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            ),
            label = ""
        )
        LaunchedEffect(Unit) {
            anim = true
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { categoryShow.value = !categoryShow.value })
        ) {
            Box(
                modifier = Modifier
                    .padding(top = categoryTop)
                    .fillMaxSize()
                    .graphicsLayer { alpha = animateAlpha }
                    .background(Color.Black)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = categoryTop)
                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp))
                .border(2.dp, MaterialTheme.colorScheme.primary)
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
                .height(animatedBoxHeight)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(MainActivity.category.size) { index ->
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                        OutlinedButton(
                            onClick = {
                                activeCategory.intValue = index
                                MainActivity.activeCategory = activeCategory.intValue
                                categoryShow.value = !categoryShow.value
                            },
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
                                if (URLUtil.isValidUrl(MainActivity.category[index].image)) {
                                    Image(
                                        alignment = Alignment.Center,
                                        painter = rememberAsyncImagePainter(MainActivity.stepUser.pictureURL),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(64.dp)
                                    )
                                } else {
                                    Image(
                                        alignment = Alignment.Center,
                                        painter = painterResource(id = R.drawable.ic_test),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(64.dp)
                                    )
                                }
                                Text(
                                    text = MainActivity.category[index].name,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .padding(0.dp, 10.dp, 0.dp, 0.dp)
                                )
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                    }
                }

            }
        }
    }
}
