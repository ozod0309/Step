package com.miki.step

object StepAPI {
    const val MAIN = "Main"
    const val LANGUAGE = "Language"
    const val SETTINGS = "Settings"
    const val TEST = "Test"
}

data class LanguageCode(
    val id: Int,
    val name: String
)

object Languages {
    val LanguageCodes: ArrayList<LanguageCode> = arrayListOf(
        LanguageCode(1, "Uzbek"),
        LanguageCode(2, "Russian")
    )
}