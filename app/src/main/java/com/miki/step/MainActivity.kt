package com.miki.step

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miki.step.lib.APIURLS
import com.miki.step.lib.LocaleHelper
import com.miki.step.lib.PostData
import com.miki.step.lib.PreferencesKeys
import com.miki.step.lib.RegistrationTypes
import com.miki.step.lib.SharedPreference
import com.miki.step.lib.StepFragments
import com.miki.step.lib.StepGlobal
import com.miki.step.lib.Test
import com.miki.step.lib.URLDownload
import com.miki.step.lib.User
import com.miki.step.lib.toStepUser
import com.miki.step.lib.toTest
import com.miki.step.ui.theme.StepTheme
import org.json.JSONObject
import java.util.Locale

class MainActivity : ComponentActivity() {

    companion object {
        var userRegistration = RegistrationTypes.UNREGISTERED
        var langCode: String = ""
        var stepUser = User()
        var tests = arrayListOf<Test>()
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val sharedPreference = SharedPreference(applicationContext)
        userRegistration = sharedPreference.getValueInt(PreferencesKeys.USER_REGISTRATION)
        langCode = sharedPreference.getValueString(PreferencesKeys.LANG_CODE).toString()
        val startUI: String =
            if (sharedPreference.getValueBoolean(PreferencesKeys.NOT_FIRST_RUN) && langCode.isNotEmpty()) {
                when (userRegistration) {
                    RegistrationTypes.REGISTERED -> {
                        stepUser =
                            sharedPreference.getValueString(PreferencesKeys.USER)!!.toStepUser()
                        StepFragments.MAIN
                    }

                    else -> StepFragments.SIGN_IN
                }
            } else {
                sharedPreference.save(PreferencesKeys.NOT_FIRST_RUN, true)
                StepFragments.LANGUAGE
            }
        setContent {
            val navController = rememberNavController()
            val startDestination by remember { mutableStateOf(startUI) }
            StepTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(StepFragments.MAIN) {
                            val context = LocalContext.current
                            MainUI(LocalContext.current).UI(
                                onStartTest = { testID ->
                                    URLDownload.urlDownload(
                                        context = context,
                                        url = APIURLS.GET_TEST,
                                        data = arrayListOf(
                                            PostData(StepGlobal.ID, testID.toString())
                                        ),
                                        onResult = { result ->
                                            try {
                                                val json = JSONObject(result)
                                                tests =
                                                    json.optJSONArray(StepGlobal.DATA)!!.toTest()
                                                navController.navigate(StepFragments.TEST)
                                            } catch (e: Exception) {
                                                Toast.makeText(
                                                    context,
                                                    "Error loading tests",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    )
                                },
                                onSettings = {
                                    navController.navigate(StepFragments.SETTINGS)
                                },
                                onInviteFriends = {

                                },
                                onShare = {

                                },
                                onCoders = {

                                },
                                onLogout = {
                                    stepUser = User()
                                    userRegistration = RegistrationTypes.UNREGISTERED
                                    sharedPreference.save(PreferencesKeys.USER, "")
                                    sharedPreference.save(
                                        PreferencesKeys.USER_REGISTRATION,
                                        userRegistration
                                    )
                                    navController.navigate(StepFragments.SIGN_IN)
                                }
                            )
                        }
                        composable(StepFragments.LANGUAGE) {
                            LanguageUI(LocalContext.current).UI(onClick = {
                                sharedPreference.save(
                                    PreferencesKeys.LANG_CODE,
                                    langCode
                                )
                                if (userRegistration == RegistrationTypes.REGISTERED) {
                                    navController.navigate(StepFragments.MAIN)
                                } else {
                                    navController.navigate(StepFragments.SIGN_IN)
                                }
                                applyNewLocale(Locale(langCode, langCode))
                            })
                        }
                        composable(StepFragments.SETTINGS) {
                            SettingsUI(LocalContext.current).UI(
                                onBackPressed = {
                                    navController.navigate(StepFragments.MAIN)
                                },
                                onLanguageChange = {lang ->
                                    langCode = lang
                                    sharedPreference.save(PreferencesKeys.LANG_CODE, lang)
                                    LocaleHelper().setLocale(this@MainActivity, langCode)
                                    recreate()
//                                    applyNewLocale(Locale(lang, lang))
                                }
                            )
                        }
                        composable(StepFragments.TEST) {
                            TestUI(LocalContext.current).UI(
                                onBackPressed = {
                                    navController.navigate(StepFragments.MAIN)
                                },
                                onTimeOut = {
                                    navController.navigate(StepFragments.RESULT)
                                },
                                onFinishTest = {
                                    navController.navigate(StepFragments.RESULT)
                                }
                            )
                        }
                        composable(StepFragments.RESULT) {
                            ResultUI(LocalContext.current).UI(
                                onDone = {
                                    navController.navigate(StepFragments.MAIN)
                                }
                            )
                        }
                        composable(StepFragments.SIGN_IN) {
                            SignInUI(LocalContext.current, onClick = {
                                if (it) {
                                    sharedPreference.save(PreferencesKeys.USER, stepUser.toJSON())
                                    sharedPreference.save(
                                        PreferencesKeys.USER_REGISTRATION,
                                        RegistrationTypes.REGISTERED
                                    )
                                    navController.navigate(StepFragments.MAIN)
                                } else {
                                    navController.navigate(StepFragments.SIGN_IN)
                                }
                            }).UI()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        applyNewLocale(Locale(langCode, langCode))
    }

    override fun attachBaseContext(newBase: Context?) {
        val sharedPref =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(newBase!!)
        langCode = sharedPref?.getString(PreferencesKeys.LANG_CODE, "").toString()
        super.attachBaseContext(newBase.applyNewLocale(Locale(langCode, langCode)))
    }

    private fun Context.applyNewLocale(locale: Locale): Context {
        val config = this.resources.configuration
        val sysLocale = config.locales.get(0)
        if (sysLocale.language != locale.language) {
            Locale.setDefault(locale)
            config.setLocale(locale)
            config.setLayoutDirection(locale)
        }
        return createConfigurationContext(config)
    }
}