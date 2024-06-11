package com.miki.step.lib

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.miki.step.MainActivity
import com.zumo.mikitodo.libs.PermissionArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PermissionManager(
    val context: Context
) : AppCompatActivity() {

    var onGranted: (() -> Unit)? = {}
    var onDenied: (() -> Unit)? = {}

    fun onResult(isGranted: Boolean) {
        if(isGranted) {
            onGranted!!.invoke()
        } else {
            onDenied!!.invoke()
        }
    }

    fun requestPermission(
        permissionIndex: Int,
    ) {
        if(Build.VERSION.SDK_INT >= PermissionArray[permissionIndex].minSDK && Build.VERSION.SDK_INT <= PermissionArray[permissionIndex].maxSDK) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    PermissionArray[permissionIndex].name
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onResult(true)
            } else {
                CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                    MainActivity.requestPermissionLauncher.launch(
                        PermissionArray[permissionIndex].name
                    )
                }
            }
        } else {
            onResult(true)
        }
    }
}