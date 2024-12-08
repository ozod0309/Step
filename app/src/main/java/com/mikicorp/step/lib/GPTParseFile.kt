package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mikicorp.step.MainActivity
import java.io.File

class GPTParseFile {
    var onSuccess: ((result: String) -> Unit)? = {}
    var onError: (() -> Unit)? = {}

    fun openFileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        MainActivity.requestGPTParseLauncher.launch(intent)
    }

    @SuppressLint("Range", "Recycle")
    fun handleSelectedFile(context: Context, uri: Uri) {
        try {
            URLDownload.uploadFile(
                url = ApiURLS.AI_URL,
                file = File(uri.path.toString())
            ) { success, result ->
                if(success)
                    onSuccess!!.invoke(result.toString())
                else
                    onError!!.invoke()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError!!.invoke()
        }
    }
}