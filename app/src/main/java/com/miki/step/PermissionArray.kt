package com.zumo.mikitodo.libs

import android.Manifest

val PermissionArray = listOf(
    MikiPermission(
        Manifest.permission.READ_PHONE_STATE,
        1,
        99
    ),
    MikiPermission(
        Manifest.permission.INTERNET,
        1,
        99
    ),
    MikiPermission(
        Manifest.permission.RECORD_AUDIO,
        1,
        99
    ),
    MikiPermission(
        Manifest.permission.READ_CONTACTS,
        1,
        99
    ),
    MikiPermission(
        Manifest.permission.SEND_SMS,
        1,
        99
    ),
    MikiPermission(
        Manifest.permission.POST_NOTIFICATIONS,
        33,
        99
    ),
    MikiPermission(
        Manifest.permission.GET_ACCOUNTS,
        1,
        99
    )
)


object PermissionKeys {
    const val READ_PHONE_STATE = 0
    const val INTERNET = 1
    const val RECORD_AUDIO = 2
    const val READ_CONTACTS = 3
    const val SEND_SMS = 4
    const val POST_NOTIFICATIONS = 5
    const val GET_ACCOUNTS = 6
}

data class MikiPermission(
    val name: String,
    val minSDK: Int,
    val maxSDK: Int,
    var asked: Boolean = false,
    var granted: Boolean = false,
)