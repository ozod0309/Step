package com.miki.step

import android.os.Bundle
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
import com.miki.step.ui.theme.StepTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()
            var startDestination by remember { mutableStateOf(StepAPI.LANGUAGE) }
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
                            LanguageUI(applicationContext).UI()
                        }
                        composable(StepAPI.SETTINGS) {
                            SettingsUI(applicationContext).UI()
                        }
                        composable(StepAPI.TEST) {
                            TestUI(applicationContext).UI()
                        }
                    }
                }
            }
        }
    }

}