package com.miki.step.lib

import org.json.JSONObject

object StepAPI {
    const val MAIN = "Main"
    const val LANGUAGE = "Language"
    const val SETTINGS = "Settings"
    const val TEST = "Test"
    const val SIGN_IN = "SignIn"
}

data class User(
    val accountName: String = "",
    val name: String = "",
    val surname: String = "",
    val googleToken: String = "",
    val stepToken: String = "",
    val pictureURL: String = ""
) {
    fun toJSON(): String {
        val json: JSONObject = JSONObject()
            .put(PreferencesKeys.GOOGLE_ACCOUNT_NAME, this.accountName)
            .put(PreferencesKeys.NAME, this.name)
            .put(PreferencesKeys.SURNAME, this.surname)
            .put(PreferencesKeys.GOOGLE_TOKEN, this.googleToken)
            .put(PreferencesKeys.STEP_TOKEN, this.stepToken)
            .put(PreferencesKeys.PICTURE_URL, this.pictureURL)
        return json.toString()
    }

    fun getFullName(): String {
        return "${this.name} ${this.surname}"
    }
}

fun String.toStepUser(): User {
    val json = try {
         JSONObject(this)
    } catch (e: Exception) {
        JSONObject()
    }
    return User(
        accountName = json.optString(PreferencesKeys.GOOGLE_ACCOUNT_NAME),
        name = json.optString(PreferencesKeys.NAME),
        surname = json.optString(PreferencesKeys.SURNAME),
        googleToken = json.optString(PreferencesKeys.GOOGLE_TOKEN),
        stepToken = json.optString(PreferencesKeys.STEP_TOKEN),
        pictureURL = json.optString(PreferencesKeys.PICTURE_URL)
    )
}

data class LanguageCode(
    val code: String,
    val name: String
)

val LanguageCodes: ArrayList<LanguageCode> = arrayListOf(
    LanguageCode("uz", "Uzbek"),
    LanguageCode("ru", "Russian"),
    LanguageCode("en", "English")
)

data class Answer(
    val answer: String,
    val image: String,
    val correct: Boolean
)
data class Test(
    val question: String,
    val image: String,
    val answer: ArrayList<Answer>
)