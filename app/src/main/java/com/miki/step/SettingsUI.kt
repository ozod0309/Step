package com.miki.step

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.ListPref
import com.miki.step.MainActivity.Companion.dataStore

class SettingsUI(val context: Context) {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun UI() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.settings),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            },
            content = {innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    PrefsScreen(dataStore = LocalContext.current.dataStore) {
                        prefsItem {
                            ListPref(
                                key = "l1",
                                title = "ListPref example",
                                summary = "Opens up a dialog of options",
                                entries = mapOf(
                                    "0" to "Entry 1",
                                    "1" to "Entry 2",
                                    "2" to "Entry 3",
                                    "3" to "Entry 4",
                                    "4" to "Entry 5"
                                )
                            )

                        }
                    }
                }

            }
        )
    }


}