package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mikicorp.step.MainActivity
import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.FileInputStream


class GPTParseFile {
    var onSuccess: ((docText: String) -> Unit)? = {}
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
//        if(!Uri.EMPTY.equals(uri))
//            onSuccess!!.invoke(uri)
//        else
//            onError!!.invoke("Error Open File")

        try {
            val docFile = context.contentResolver?.openFileDescriptor(uri, "r")
            val docStream = FileInputStream(docFile?.fileDescriptor)
            val targetDoc = XWPFDocument(docStream)
            val wordExtractor = XWPFWordExtractor(targetDoc)
            docStream.close()
            onSuccess!!.invoke(wordExtractor.text)
        } catch (e: Exception) {
            e.printStackTrace()
            onError!!.invoke("File Open Error")
        }
    }
}