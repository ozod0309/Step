package com.miki.step.lib

import com.miki.step.MainActivity
import org.json.JSONArray
import org.json.JSONObject

object StepGlobal {
    const val ID = "Id"
    const val DATA = "data"
    const val NAME = "name"
    const val CATEGORY_ID = "category_id"
    const val SUBCATEGORY_ID = "subcategory_id"
    const val ANSWER_LIST = "answer_list"
    const val QUESTION_ID = "question_id"
    const val IS_CORRECT = "is_correct"
    const val IMAGE = "image"
}

object StepFragments {
    const val MAIN = "Main"
    const val LANGUAGE = "Language"
    const val SETTINGS = "Settings"
    const val TEST = "Test"
    const val RESULT = "Result"
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

fun JSONArray.toTest() {
    for (i in 0 until this.length()) {
        val item = this[i]
        val test = Test(
            question =
        )
    }
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

