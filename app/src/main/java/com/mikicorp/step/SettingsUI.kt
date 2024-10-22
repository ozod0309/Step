package com.mikicorp.step

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.ListPref
import com.jamal.composeprefs3.ui.prefs.TextPref
import com.mikicorp.step.MainActivity.Companion.dataStore
import com.mikicorp.step.lib.LanguageCodes
import com.mikicorp.step.lib.SettingsKeys

class SettingsUI(val context: Context?) {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun UI(
        onBackPressed: () -> Unit = {},
        onLanguageChange: (lang: String) -> Unit = {}
    ) {
        LaunchedEffect(Unit) {
            savePref(SettingsKeys.LANG, MainActivity.langCode)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.settings),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                onBackPressed()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = ""
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    PrefsScreen(dataStore = LocalContext.current.dataStore) {
                        prefsItem {
                            Row {
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .align(Alignment.CenterVertically),
                                    imageVector = Icons.Filled.Language,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = ""
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                ListPref(
                                    key = SettingsKeys.LANG,
                                    title = stringResource(id = R.string.language),
                                    entries = LanguageCodes.associate { it.code to it.name },
                                    onValueChange = { value ->
                                        onLanguageChange(value)
                                    }
                                )
                            }
                        }
                        prefsItem {
                            Row {
                                Spacer(modifier = Modifier.width(5.dp))
                                Icon(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .align(Alignment.CenterVertically),
                                    imageVector = Icons.Filled.Code,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = context!!.resources.getString(R.string.coders)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                TextPref(
                                    title = stringResource(id = R.string.coders)
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    private suspend fun savePref(key: String, value: String) {
        context!!.dataStore.edit {preference ->
            preference[stringPreferencesKey(key)] = value

        }
    }
}

@Preview
@Composable
fun SettingsUIPreview() {
    SettingsUI(null).UI()
}