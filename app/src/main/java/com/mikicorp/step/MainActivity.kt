package com.mikicorp.step

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikicorp.step.lib.ApiURLS
import com.mikicorp.step.lib.AppNotification
import com.mikicorp.step.lib.Category
import com.mikicorp.step.lib.GPTParseFile
import com.mikicorp.step.lib.InternetAvailable
import com.mikicorp.step.lib.LocaleHelper
import com.mikicorp.step.lib.PermissionManager
import com.mikicorp.step.lib.PostData
import com.mikicorp.step.lib.PreferencesKeys
import com.mikicorp.step.lib.ReadMSFiles
import com.mikicorp.step.lib.ReadPDFFiles
import com.mikicorp.step.lib.RegistrationTypes
import com.mikicorp.step.lib.SharedPreference
import com.mikicorp.step.lib.SimUtil
import com.mikicorp.step.lib.StepFragments
import com.mikicorp.step.lib.StepGlobal
import com.mikicorp.step.lib.Test
import com.mikicorp.step.lib.URLDownload
import com.mikicorp.step.lib.User
import com.mikicorp.step.lib.toCategories
import com.mikicorp.step.lib.toStepUser
import com.mikicorp.step.lib.toTest
import com.mikicorp.step.mlkit.MLKit
import com.mikicorp.step.ui.theme.StepTheme
import com.zumo.mikitodo.libs.PermissionKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class MainActivity : ComponentActivity() {

    companion object {
        var androidId: String = ""
        var userRegistration = RegistrationTypes.UNREGISTERED
        var langCode: String = ""
        var stepUser = User()
        var category = arrayListOf<Category>()
        var activeCategory = 0
        var activeSubcategory = 0
        var testSessionID = 0
        var tests = arrayListOf<Test>()
        var bottomBarSelector = 0
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
        lateinit var permissionManager: PermissionManager
        lateinit var requestMSFileLauncher: ActivityResultLauncher<Intent>
        lateinit var requestPDFFileLauncher: ActivityResultLauncher<Intent>
        lateinit var requestGPTParseLauncher: ActivityResultLauncher<Intent>
        lateinit var readMSFiles: ReadMSFiles
        lateinit var readPDFFiles: ReadPDFFiles
        lateinit var gptParseFile: GPTParseFile

    }

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        permissionManager = PermissionManager()
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                permissionManager.onResult(isGranted)
            }
        readMSFiles = ReadMSFiles()
        readPDFFiles = ReadPDFFiles()
        gptParseFile = GPTParseFile()
        requestMSFileLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let { uri ->
                        readMSFiles.handleSelectedFile(this, uri)
                    }
                }
            }
        requestPDFFileLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let { uri ->
                        readPDFFiles.handleSelectedFile(uri)
                    }
                }
            }
        requestGPTParseLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.data?.let { uri ->
                        gptParseFile.handleSelectedFile(this, uri)
                    }
                }
            }
        val sharedPreference = SharedPreference(applicationContext)
        var docText = ""
        androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        userRegistration = sharedPreference.getValueInt(PreferencesKeys.USER_REGISTRATION)
        langCode = sharedPreference.getValueString(PreferencesKeys.LANG_CODE).toString()
        val context = this
        val notFirstRun = sharedPreference.getValueBoolean(PreferencesKeys.NOT_FIRST_RUN)
        val startUI: String =
            if (notFirstRun && langCode.isNotEmpty()) {
                when (userRegistration) {
                    RegistrationTypes.REGISTERED -> {
                        stepUser =
                            sharedPreference.getValueString(PreferencesKeys.USER)!!.toStepUser()
                        if (loadCategories(this))
                            StepFragments.MAIN
                        else {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.server_error),
                                Toast.LENGTH_SHORT
                            ).show()

                            StepFragments.ERROR
                        }
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
            val newNotification = remember {
                mutableStateOf(false)
            }
            var newNotificationText = ""
            StepTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.tertiary
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(StepFragments.MAIN) {
                            MainUI(LocalContext.current).UI(
                                onStartTest = { subCategoryId ->
                                    activeSubcategory = subCategoryId
                                    URLDownload.urlDownload(
                                        context = context,
                                        sURL = ApiURLS.GET_TEST,
                                        requestBody = arrayListOf(
                                            PostData(
                                                StepGlobal.CATEGORY_ID,
                                                category[activeCategory].id.toString()
                                            ),
                                            PostData(
                                                StepGlobal.SUBCATEGORY_ID,
                                                category[activeCategory].subCategory[subCategoryId].id.toString()
                                            )
                                        ),
                                        onResult = { result ->
                                            try {
                                                val json = JSONObject(result)
                                                val data = json.optJSONObject(StepGlobal.DATA)!!
                                                testSessionID = data.optInt(StepGlobal.SESSION_ID)
                                                tests =
                                                    data.optJSONArray(StepGlobal.QUESTION_LIST)!!
                                                        .toTest()
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
                                onProfile = {

                                },
                                onSettings = {
                                    navController.navigate(StepFragments.SETTINGS)
                                },
                                onInviteFriends = {
                                    permissionManager.onGranted = {
                                        navController.navigate(StepFragments.INVITE_FRIENDS)
                                    }
                                    permissionManager.onDenied = {
                                        Toast.makeText(
                                            context,
                                            context.resources.getString(R.string.permission_denied),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    permissionManager
                                        .requestPermission(context, PermissionKeys.READ_CONTACTS)
                                },
                                onShare = {
                                    ShareCompat
                                        .IntentBuilder(this@MainActivity)
                                        .setType("text/plain")
                                        .setChooserTitle(resources.getString(R.string.share))
                                        .setText("http://play.google.com/store/apps/details?id=" + context.packageName)
                                        .startChooser()
                                },
                                onRateUs = {
                                    val uri =
                                        Uri.parse("market://details?id=" + context.packageName)
                                    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
                                    try {
                                        startActivity(myAppLinkToMarket)
                                    } catch (e: ActivityNotFoundException) {
                                        Toast.makeText(
                                            context,
                                            "Impossible to find an application for the market",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                },
                                onNotification = { message ->
                                    newNotification.value = true
                                    newNotificationText = message
                                },
                                onCreateTest = {
                                    navController.navigate(StepFragments.CREATE_TEST_SOURCE)
//                                    permissionManager.onGranted = {
//                                        navController.navigate(StepFragments.MY_STEP)
//                                    }
//                                    permissionManager.onDenied = {
//                                        navController.navigate(StepFragments.ERROR)
//                                    }
//                                    permissionManager.requestPermission(context, PermissionKeys.CAMERA)
                                },
                                onEditTest = {
                                    permissionManager.onGranted = {
                                        navController.navigate(StepFragments.OCR)
                                    }
                                    permissionManager.onDenied = {
                                        navController.navigate(StepFragments.ERROR)
                                    }
                                    permissionManager.requestPermission(context, PermissionKeys.CAMERA)
                                },
                                onDeleteTest = {
                                    permissionManager.onGranted = {
                                        navController.navigate(StepFragments.OCR)
                                    }
                                    permissionManager.onDenied = {
                                        navController.navigate(StepFragments.ERROR)
                                    }
                                    permissionManager.requestPermission(context, PermissionKeys.CAMERA)
                                },
                                onLogout = {
                                    URLDownload.urlDownload(
                                        context = context,
                                        sURL = ApiURLS.LOGOUT,
                                        requestBody = arrayListOf(
                                            PostData(StepGlobal.ID_TOKEN, stepUser.stepToken)
                                        ),
                                        onResult = { result ->
                                            val json = try {
                                                JSONObject(result)
                                            } catch (e: Exception) {
                                                JSONObject()
                                            }
                                            if (json.has(StepGlobal.SUCCESS)) {
                                                stepUser = User()
                                                userRegistration =
                                                    RegistrationTypes.UNREGISTERED
                                                sharedPreference.save(PreferencesKeys.USER, "")
                                                sharedPreference.save(PreferencesKeys.GOOGLE_TOKEN, "")
                                                sharedPreference.save(
                                                    PreferencesKeys.USER_REGISTRATION,
                                                    userRegistration
                                                )
                                                navController.navigate(StepFragments.SIGN_IN)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    resources.getString(R.string.server_error),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate(StepFragments.MAIN)
                                            }
                                        }
                                    )
                                }
                            )
                        }
                        composable(StepFragments.LANGUAGE) {
                            LanguageUI(LocalContext.current).UI(onClick = {
                                sharedPreference.save(
                                    PreferencesKeys.LANG_CODE,
                                    langCode
                                )
                                LocaleHelper().setLocale(this@MainActivity, langCode)
                                recreate()
                                if(!notFirstRun) {
                                    permissionManager.onGranted = {
                                        Toast.makeText(
                                            context,
                                            context.resources.getString(R.string.permission_denied),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    permissionManager.onDenied = {
                                        Toast.makeText(
                                            context,
                                            context.resources.getString(R.string.permission_denied),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    permissionManager.requestPermission(context, PermissionKeys.POST_NOTIFICATIONS)
                                }
                                if (userRegistration == RegistrationTypes.REGISTERED) {
                                    navController.navigate(StepFragments.MAIN)
                                } else {
                                    navController.navigate(StepFragments.SIGN_IN)
                                }
                            })
                        }
                        composable(StepFragments.SETTINGS) {
                            SettingsUI(LocalContext.current).UI(
                                onBackPressed = {
                                    navController.navigate(StepFragments.MAIN)
                                },
                                onLanguageChange = { lang ->
                                    langCode = lang
                                    sharedPreference.save(PreferencesKeys.LANG_CODE, lang)
                                    LocaleHelper().setLocale(this@MainActivity, langCode)
                                    recreate()
                                }
                            )
                        }
                        composable(StepFragments.INVITE_FRIENDS) {

                            InviteFriendsUI(LocalContext.current).UI(
                                onBackPressed = {
                                    navController.navigate(StepFragments.MAIN)
                                },
                                onInvite = { number ->
                                    permissionManager.onGranted = {
                                        permissionManager.onGranted = {
                                            permissionManager.onGranted = {
                                                SimUtil(
                                                    onSMSDelivered = {
                                                        Toast.makeText(
                                                            context,
                                                            context.resources.getString(R.string.sms_delivered),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    },
                                                    onSMSSend = {
                                                        Toast.makeText(
                                                            context,
                                                            context.resources.getString(R.string.sms_sent),
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                ).sendSMS(
                                                    context = context,
                                                    message = context.resources.getString(R.string.invite_sms),
                                                    phoneNumber = number,
                                                    simSelected = 0
                                                )
                                                navController.navigate(StepFragments.MAIN)
                                            }
                                            permissionManager.onDenied = {
                                                Toast.makeText(
                                                    context,
                                                    context.resources.getString(R.string.permission_denied),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            permissionManager.requestPermission(context, PermissionKeys.SEND_SMS)
                                        }
                                        permissionManager.onDenied = {
                                            Toast.makeText(
                                                context,
                                                context.resources.getString(R.string.permission_denied),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        permissionManager.requestPermission(
                                            context, PermissionKeys.READ_PHONE_NUMBERS
                                        )
                                    }
                                    permissionManager.onDenied = {
                                        Toast.makeText(
                                            context,
                                            context.resources.getString(R.string.permission_denied),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    permissionManager.requestPermission(
                                        context, PermissionKeys.READ_PHONE_STATE
                                    )
                                }
                            )
                        }
                        composable(StepFragments.TEST) {
                            TestUI(LocalContext.current).UI(
                                onBackPressed = {
                                    navController.navigate(StepFragments.MAIN)
                                },
                                onTimeOut = {
                                    checkForTestResults(
                                        context,
                                        onResult = { result ->
                                            if (result) {
                                                navController.navigate(StepFragments.RESULT)
                                            } else {
                                                navController.navigate(StepFragments.ERROR)
                                            }
                                        }
                                    )
                                },
                                onFinishTest = {
                                    checkForTestResults(
                                        context,
                                        onResult = { result ->
                                            if (result) {
                                                navController.navigate(StepFragments.RESULT)
                                            } else {
                                                navController.navigate(StepFragments.ERROR)
                                            }
                                        }
                                    )
                                },
                                onError = {error ->
                                    navController.navigate(StepFragments.ERROR)
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
                                    URLDownload.urlDownload(
                                        context = context,
                                        sURL = ApiURLS.SIGN_IN_GOOGLE,
                                        requestBody = arrayListOf(
                                            PostData(StepGlobal.ID_TOKEN, stepUser.googleToken)
                                        ),
                                        onResult = { result ->
                                            val json = try {
                                                JSONObject(result)
                                            } catch (e: Exception) {
                                                JSONObject()
                                            }
                                            if (json.optBoolean(StepGlobal.SUCCESS)) {
                                                val data = json.optJSONObject(StepGlobal.DATA)!!
                                                val user = data.optJSONObject(StepGlobal.USER)!!
                                                stepUser.name = user.optString(StepGlobal.NAME)
                                                stepUser.surname =
                                                    user.optString(StepGlobal.SURNAME)
                                                stepUser.stepToken =
                                                    data.optString(StepGlobal.ACCESS_TOKEN)
                                                stepUser.tokenType =
                                                    data.optString(StepGlobal.TOKEN_TYPE)
                                                sharedPreference.save(
                                                    PreferencesKeys.USER,
                                                    stepUser.toJSON()
                                                )
                                                sharedPreference.save(
                                                    PreferencesKeys.USER_REGISTRATION,
                                                    RegistrationTypes.REGISTERED
                                                )
                                                loadCategories(context)
                                                navController.navigate(StepFragments.MAIN)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    resources.getString(R.string.server_error),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                navController.navigate(StepFragments.SIGN_IN)
                                            }
                                        }
                                    )
                                } else {
//                                    Toast.makeText(applicationContext, resources.getString(R.string.server_error), Toast.LENGTH_SHORT).show()
                                    navController.navigate(StepFragments.SIGN_IN)
                                }
                            }).UI()
                        }
                        composable(StepFragments.CREATE_TEST_SOURCE) {
                            CreateTestSourceUI(LocalContext.current).UI(
                                onMSDocs = {
                                    readMSFiles.onSuccess = {result ->
                                        docText = result
                                        navController.navigate(StepFragments.MSDOCS)
                                    }
                                    readMSFiles.onError = {
                                        navController.navigate(StepFragments.ERROR)
                                    }
                                    readMSFiles.openFileSelector()
                                },
                                onPDFDocs = {
                                    readPDFFiles.onSuccess = {result ->
                                        docText = result
                                        navController.navigate(StepFragments.MSDOCS)
                                    }
                                    readPDFFiles.onError = {
                                        navController.navigate(StepFragments.ERROR)
                                    }
                                    readPDFFiles.openFileSelector()

                                },
                                onOCR = {
                                    navController.navigate(StepFragments.OCR)
                                },
                                onGPTParse = {
                                    gptParseFile.onSuccess = {result ->
                                        docText = result
                                        navController.navigate(StepFragments.MSDOCS)
                                    }
                                    gptParseFile.onError = {
                                        runOnUiThread {
                                            navController.navigate(StepFragments.ERROR)
                                        }
                                    }
                                    gptParseFile.openFileSelector()
                                },
                                onClose = {
                                    navController.navigate(StepFragments.MAIN)
                                }
                            )
                        }
                        composable(StepFragments.MSDOCS) {
                            CreateTestUI(LocalContext.current, docText).UI(
                                onSubmit = { docList ->

                                },
                                onBackPressed = {
                                    navController.navigate(StepFragments.MAIN)
                                },
                                onClose = {
                                    navController.navigate(StepFragments.MAIN)
                                }
                            )
                        }
                        composable(StepFragments.OCR) {
                            MLKit(LocalContext.current).UI(
                                onDone = {
                                    navController.navigate(StepFragments.MAIN)
                                }
                            )
                        }
                        composable(StepFragments.ERROR) {
                            ErrorUI(LocalContext.current).UI(
                                onDone = {
                                    navController.navigate(StepFragments.MAIN)
                                }
                            )
                        }
                    }
                }
                if (newNotification.value) {
                    AppNotification.Add(
                        icon = AppNotification.IconType.NOTIFICATION,
                        message = newNotificationText,
                        onFinish = {
                            newNotification.value = false
                        }
                    )
                }
            }
        }
    }

    private fun checkForTestResults(context: Context, onResult: (result: Boolean) -> Unit) {
        val data = JSONArray()
        tests.forEach { item ->
            val json = JSONObject()
                .put(StepGlobal.QUESTION_ID, item.id)
                .put(StepGlobal.ANSWER_ID, item.answered)
            data.put(json)
        }
        URLDownload.urlDownload(
            context = context,
            sURL = "${ApiURLS.GET_TEST}/${testSessionID}/finish",
            requestBody = arrayListOf(
                PostData(StepGlobal.DATA, data.toString())
            ),
            onResult = { result ->
                val jsonResult = try {
                    JSONObject(result)
                } catch (e: Exception) {
                    JSONObject()
                }
                if (jsonResult.optBoolean(StepGlobal.SUCCESS)) {
                    val dataResult = jsonResult.optJSONObject(StepGlobal.DATA)
                    if (dataResult!!.optInt(StepGlobal.SESSION_ID) == testSessionID) {
                        val answerResults = try {
                            dataResult.getJSONArray(StepGlobal.RESULTS)
                        } catch (e:Exception) {
                            JSONArray()
                        }
                        for (i in 0 until answerResults!!.length()) {
                            val item = answerResults[i] as JSONObject
                            tests.find { it.id == item.optInt(StepGlobal.QUESTION_ID) }!!.isCorrect =
                                item.optInt(StepGlobal.IS_CORRECT) == 1
                        }
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                } else {
                    onResult(false)
                }
            }
        )

    }

    private fun loadCategories(context: Context): Boolean {
        var result = false
        if (InternetAvailable.internetAvailable(context)) {
            val urlResult = runBlocking {
                withContext(Dispatchers.IO) {
                    val urlResult = URLDownload.getInetData(ApiURLS.GET_CATS)
                    urlResult
                }
            }
            val json = try {
                JSONObject(urlResult)
            } catch (e: Exception) {
                JSONObject()
            }
            if (json.has(StepGlobal.SUCCESS)) {
                var res = true
                try {
                    val data = json.optJSONArray(StepGlobal.DATA)!!
                    category = data.toCategories()
                } catch (e: Exception) {
                    res = false
                } finally {
                    result = res
                }
            } else {
                result = false
            }
        }
        return result
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