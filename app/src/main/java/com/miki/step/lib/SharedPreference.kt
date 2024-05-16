package com.miki.step.lib

import android.content.Context
import android.content.SharedPreferences
import java.util.Date

class SharedPreference(val context: Context?, prefName: String = "") {

    private val sharedPref: SharedPreferences = if(prefName.isEmpty()) {
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context!!)}
    else {
        context!!.getSharedPreferences(prefName, 0)
    }

    fun save(keyName: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(keyName, text)
        editor.apply()
    }

    fun save(keyName: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(keyName, value)
        editor.apply()
    }

    fun save(keyName: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(keyName, status)
        editor.apply()
    }

    fun save(keyName: String, value: Long) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putLong(keyName, value)
        editor.apply()
    }

    fun save(keyName: String, date: Date) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putLong(keyName, date.time)
        editor.apply()
    }

    fun getValueString(keyName: String): String? {
        return sharedPref.getString(keyName, "")
    }

    fun getValueInt(keyName: String): Int {
        return sharedPref.getInt(keyName, 0)
    }

    fun getValueDate(keyName: String): Date {
        val lDate = sharedPref.getLong(keyName, 0)
        val date = Date()
        date.time = lDate
        return date
    }

    fun getValueBoolean(keyName: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(keyName, defaultValue)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun removeValue(keyName: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(keyName)
        editor.apply()
    }

    fun containKey(keyName: String): Boolean {
        return sharedPref.contains(keyName)
    }

}