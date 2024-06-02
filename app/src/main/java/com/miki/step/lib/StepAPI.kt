package com.miki.step.lib

import org.json.JSONArray
import org.json.JSONObject

object StepGlobal {
    const val ID = "id"
    const val DATA = "data"
    const val NAME = "name"
    const val SURNAME = "surname"
    const val CATEGORY = "category"
    const val SUBCATEGORIES = "subcategories"
    const val CATEGORY_ID = "category_id"
    const val SUBCATEGORY_ID = "subcategory_id"
    const val ANSWER_LIST = "answer_list"
    const val QUESTION_ID = "question_id"
    const val IS_CORRECT = "is_correct"
    const val IMAGE = "image"

    const val RESULT = "result"
    const val SUCCESS = "success"
    const val AUTH = "Authorization"
    const val ID_TOKEN = "id_token"
    const val ACCESS_TOKEN = "access_token"
    const val ANDROID_ID = "android_id"

}

object StepFragments {
    const val MAIN = "Main"
    const val LANGUAGE = "Language"
    const val SETTINGS = "Settings"
    const val TEST = "Test"
    const val RESULT = "Result"
    const val SIGN_IN = "SignIn"
    const val ERROR = "error"
}

object SettingsKeys {
    const val LANG = "lang"
}

data class User(
    val accountName: String = "",
    var name: String = "",
    var surname: String = "",
    val googleToken: String = "",
    var stepToken: String = "",
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

data class Category(
    val id: Int,
    val name: String,
    val image: String,
    val subCategory: ArrayList<SubCategory>
)

data class SubCategory(
    val id: Int,
    val name: String
)

fun JSONArray.toCategories(): ArrayList<Category> {
    val cats = arrayListOf<Category>()
    for(i in 0 until this.length()) {
        val item = this[i] as JSONObject
        val subCategories = arrayListOf<SubCategory>()
        val subCategoryList = item.optJSONArray(StepGlobal.SUBCATEGORIES)!!
        for(j in 0 until subCategoryList.length()) {
            val subItem = subCategoryList[j] as JSONObject
            subCategories.add(
                SubCategory(
                    id = subItem.optInt(StepGlobal.ID),
                    name = subItem.optString(StepGlobal.NAME)
                )
            )
        }
        cats.add(
            Category(
                id = item.optInt(StepGlobal.ID),
                name = item.optString(StepGlobal.NAME),
                image = item.optString(StepGlobal.IMAGE),
                subCategory = subCategories
            )
        )
    }
    return cats
}

data class Test(
    val id: Int,
    val question: String,
    val image: String = "",
    val answers: ArrayList<Answer>,
    var answered: Int = 0
)

data class Answer(
    val id: Int,
    val answer: String,
    val image: String = "",
    val correct: Boolean
)

fun JSONArray.toTest(): ArrayList<Test> {
    val ques = arrayListOf<Test>()
    for (i in 0 until this.length()) {
        val item = this[i] as JSONObject
        val answers = item.optJSONArray(StepGlobal.ANSWER_LIST)
        val answerList = arrayListOf<Answer>()
        for (j in 0 until answers!!.length()) {
            val answerItem = answers[j] as JSONObject
            answerList.add(
                Answer(
                    id = answerItem.optInt(StepGlobal.ID),
                    answer = answerItem.optString(StepGlobal.NAME),
                    image = answerItem.optString(StepGlobal.IMAGE),
                    correct = answerItem.optBoolean(StepGlobal.IS_CORRECT)
                )
            )
        }
        ques.add(
            Test(
                id = item.optInt(StepGlobal.ID),
                question = item.optString(StepGlobal.NAME),
                image = item.optString(StepGlobal.IMAGE),
                answers = answerList
            )
        )
    }
    return ques
}

