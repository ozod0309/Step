package com.mikicorp.step.lib

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

object AppNotification {
    @Composable
    fun Add(
        icon: ImageVector,
        message: String,
        onFinish:() -> Unit
    ) {
        val showNotification = remember {
            mutableStateOf(true)
        }

        var animStart by remember {
            mutableStateOf(false)
        }
        val animateOffset by animateDpAsState(
            targetValue = if (animStart) 0.dp else 150.dp,
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            label = ""
        )
        LaunchedEffect(Unit) {
            animStart = true
            delay(3000)
            showNotification.value = false
            onFinish()
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        )

        AnimatedVisibility(
            visible = showNotification.value,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut(animationSpec = tween(2000))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = animateOffset)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
            ) {
                    Box(
                        modifier = Modifier
                            .wrapContentHeight()
                            .wrapContentWidth()
                            .widthIn(0.dp, 150.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(15.dp, 0.dp, 0.dp, 15.dp)
                            )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(5.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = message
                            )
                        }
                    }
            }
        }
    }

    object IconType {
        val NOTIFICATION = Icons.Filled.Notifications
        val WARNING = Icons.Filled.Warning
        val ERROR = Icons.Filled.Error
    }

}