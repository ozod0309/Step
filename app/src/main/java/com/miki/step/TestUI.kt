package com.miki.step

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class TestUI(context: Context?) {
    @SuppressLint("NotConstructor")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UI() {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .background(color = Color.Green)
                        .fillMaxWidth()
                ) {
                    Text(text = "test")
                }
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .height(20.dp)
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
                        IconButton(onClick = { /*TODO*/ }) {
//                      previous
                        }
                        IconButton(onClick = { /*TODO*/ }) {
//                      done
                        }
                        IconButton(onClick = { /*TODO*/ }) {
//                      next
                        }
                    }
                )
            }
        )
    }
}
