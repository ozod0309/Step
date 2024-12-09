package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mikicorp.step.MainActivity


class GPTParseFile {
    var onSuccess: ((result: String) -> Unit)? = {}
    var onError: ((errorText: String) -> Unit)? = {}

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
                context = context,
                url = ApiURLS.AI_URL,
                fileUri = uri
            ) { success, result ->
                if(success)
                    onSuccess!!.invoke(result.toString())
                else
                    onError!!.invoke("Error Open File")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onError!!.invoke("Error Open File")
        }
    }

}