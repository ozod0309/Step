package com.miki.step

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.miki.step.lib.LanguageCodes
import com.miki.step.lib.PreferencesKeys
import com.miki.step.lib.RegistrationTypes
import com.miki.step.lib.SharedPreference
import com.miki.step.lib.StepAPI
import com.miki.step.ui.theme.StepTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : ComponentActivity() {

    companion object {
        var userRegistration = RegistrationTypes.UNREGISTERED
        var langCode: String = ""
    }

    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val sharedPreference = SharedPreference(applicationContext)
        userRegistration = sharedPreference.getValueInt(PreferencesKeys.UserRegistration)
        langCode = sharedPreference.getValueString(PreferencesKeys.LangCode).toString()
        var startUI: String =
            if (sharedPreference.getValueBoolean(PreferencesKeys.NotFirstRun) || langCode.isEmpty()) {
                when (userRegistration) {
                    RegistrationTypes.REGISTERED -> StepAPI.MAIN
                    else -> StepAPI.LOGIN
                }
            } else {
                sharedPreference.save(PreferencesKeys.NotFirstRun, true)
                StepAPI.LANGUAGE
            }
//        startUI = StepAPI.TEST
        setContent {
            val navController = rememberNavController()
            var startDestination by remember { mutableStateOf(startUI) }
            StepTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(StepAPI.MAIN) {
                            MainUI(applicationContext).UI()
                        }
                        composable(StepAPI.LANGUAGE) {
                            LanguageUI(applicationContext).UI(onClick = { index ->
                                sharedPreference.save(
                                    PreferencesKeys.LangCode,
                                    LanguageCodes[index].code
                                )
                                if (userRegistration == RegistrationTypes.REGISTERED) {
                                    navController.navigate(StepAPI.MAIN)
                                } else {
                                    navController.navigate(StepAPI.LOGIN)
                                }
                            })
                        }
                        composable(StepAPI.SETTINGS) {
                            SettingsUI(applicationContext).UI()
                        }
                        composable(StepAPI.TEST) {
                            TestUI(applicationContext).UI()
                        }
                        composable(StepAPI.LOGIN) {
                            LoginUI(LocalContext.current, onClick = {
//                                signUp()
                            }
                            ).UI()
                        }
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val sharedPref =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(newBase!!)
        langCode = sharedPref?.getString(PreferencesKeys.LangCode, "").toString()
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