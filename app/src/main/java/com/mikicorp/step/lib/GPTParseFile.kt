package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.mikicorp.step.MainActivity


class GPTParseFile {
    var onSuccess: ((uri: Uri) -> Unit)? = {}
    var onError: ((errorText: String) -> Unit)? = {}

    fun openFileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        MainActivity.requestGPTParseLauncher.launch(intent)
    }

    @SuppressLint("Range", "Recycle")
    fun handleSelectedFile(uri: Uri) {
        if(!Uri.EMPTY.equals(uri))
            onSuccess!!.invoke(uri)
        else
            onError!!.invoke("Error Open File")
    }

}