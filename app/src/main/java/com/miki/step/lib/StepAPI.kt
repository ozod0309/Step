package com.miki.step.lib

import org.json.JSONObject

object StepAPI {
    const val MAIN = "Main"
    const val LANGUAGE = "Language"
    const val SETTINGS = "Settings"
    const val TEST = "Test"
    const val LOGIN = "Login"
}

data class User(
    val accountName: String = "",
    val name: String = "",
    val surname: String = "",
    val googleToken: String = "",
    val stepToken: String = ""
) {
    fun toJSON(): JSONObject {
        val json: JSONObject = JSONObject()
            .put(PreferencesKeys.GOOGLE_ACCOUNT_NAME, this.accountName)
            .put(PreferencesKeys.NAME, this.name)
            .put(PreferencesKeys.SURNAME, this.surname)
            .put(PreferencesKeys.GOOGLE_TOKEN, this.googleToken)
            .put(PreferencesKeys.STEP_TOKEN, this.stepToken)
        return json
    }
}

fun JSONObject.parse(json: JSONObject): User {
    return User(
        accountName = this.optString(PreferencesKeys.GOOGLE_ACCOUNT_NAME),
        name = this.optString(PreferencesKeys.NAME),
        surname = this.optString(PreferencesKeys.SURNAME),
        googleToken = this.optString(PreferencesKeys.GOOGLE_TOKEN),
        stepToken = this.optString(PreferencesKeys.STEP_TOKEN)
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