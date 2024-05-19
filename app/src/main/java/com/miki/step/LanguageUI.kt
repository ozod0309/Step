package com.miki.step

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miki.step.lib.LanguageCodes

class LanguageUI(val context: Context?) {
    @Composable
    fun UI(onClick:(index: Int) -> Unit) {
        Box(modifier = Modifier.padding()) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                items(LanguageCodes.size) { index ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 10.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        onClick = {
                            MainActivity.langCode = LanguageCodes[index].code
                            onClick(index)
                        }
                    ) {
                        Text(
                            text =LanguageCodes[index].name,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onTertiary
                            )
                    }
                }
            }
        }
    }
}
