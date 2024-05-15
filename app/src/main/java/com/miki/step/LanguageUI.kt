package com.miki.step

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class LanguageUI(applicationContext: Context?) {
    @Composable
    fun UI() {
        Box(modifier = Modifier.padding()) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                items(Languages.LanguageCodes.size) {
                    Languages.LanguageCodes[it].name
                }
            }
        }
    }
}
