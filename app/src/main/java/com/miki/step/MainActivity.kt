package com.miki.step

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.miki.step.lib.LanguageCodes
import com.miki.step.lib.PreferencesKeys
import com.miki.step.lib.RegistrationTypes
import com.miki.step.lib.SharedPreference
import com.miki.step.lib.StepAPI
import com.miki.step.ui.theme.StepTheme
import java.security.SecureRandom
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

        FirebaseApp.initializeApp(applicationContext)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(applicationContext, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        val sharedPreference = SharedPreference(applicationContext)
        userRegistration = sharedPreference.getValueInt(PreferencesKeys.UserRegistration)
        langCode = sharedPreference.getValueString(PreferencesKeys.LangCode).toString()
        val startUI: String =
            if (sharedPreference.getValueBoolean(PreferencesKeys.NotFirstRun) || langCode.isEmpty()) {
                when (userRegistration) {
                    RegistrationTypes.REGISTERED -> StepAPI.MAIN
                    else -> StepAPI.LOGIN
                }
            } else {
                sharedPreference.save(PreferencesKeys.NotFirstRun, true)
                StepAPI.LANGUAGE
            }
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
                            LoginUI(applicationContext).UI(onClick = {
                                signInGoogle()
                            })
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


    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, account.displayName.toString() + " registered", Toast.LENGTH_LONG).show()
//                SavedPreference.setEmail(this, account.email.toString())
//                SavedPreference.setUsername(this, account.displayName.toString())
//                val intent = Intent(this, DashboardActivity::class.java)
//                startActivity(intent)
//                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
//            startActivity(
//                Intent(
//                    this, DashboardActivity
//                    ::class.java
//                )
//            )
//            finish()
        }
    }

}