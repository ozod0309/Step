package com.miki.step

import android.content.Context
import android.os.Bundle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miki.step.lib.LanguageCodes
import com.miki.step.lib.PreferencesKeys
import com.miki.step.lib.RegistrationTypes
import com.miki.step.lib.SharedPreference
import com.miki.step.lib.StepAPI
import com.miki.step.lib.User
import com.miki.step.ui.theme.StepTheme
import java.util.Locale

class MainActivity : ComponentActivity() {

    companion object {
        var userRegistration = RegistrationTypes.UNREGISTERED
        var langCode: String = ""
        var stepUser = User()
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
                    RegistrationTypes.REGISTERED -> StepAPI.MAIN
                    else -> StepAPI.LOGIN
                }
            } else {
                sharedPreference.save(PreferencesKeys.NOT_FIRST_RUN, true)
                StepAPI.LANGUAGE
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
                        composable(StepAPI.MAIN) {
                            MainUI(LocalContext.current).UI()
                        }
                        composable(StepAPI.LANGUAGE) {
                            LanguageUI(LocalContext.current).UI(onClick = { index ->
                                sharedPreference.save(
                                    PreferencesKeys.LANG_CODE,
                                    langCode
                                )
                                if (userRegistration == RegistrationTypes.REGISTERED) {
                                    navController.navigate(StepAPI.MAIN)
                                } else {
                                    navController.navigate(StepAPI.LOGIN)
                                }
                                applyNewLocale(Locale(langCode, langCode))
                            })
                        }
                        composable(StepAPI.SETTINGS) {
                            SettingsUI(LocalContext.current).UI()
                        }
                        composable(StepAPI.TEST) {
                            TestUI(LocalContext.current).UI()
                        }
                        composable(StepAPI.LOGIN) {
                            SignInUI(LocalContext.current, onClick = {
                                navController.navigate(StepAPI.MAIN)
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