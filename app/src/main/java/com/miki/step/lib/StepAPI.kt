package com.miki.step.lib

object StepAPI {
    const val MAIN = "Main"
    const val LANGUAGE = "Language"
    const val SETTINGS = "Settings"
    const val TEST = "Test"
    const val LOGIN = "Login"
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