package com.mikicorp.step

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class ErrorUI(val context: Context?) {
    @Composable
    fun UI(onDone: () -> Unit) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                onDone()
            }
        ) {
            Text(
                text = "Ok",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}
