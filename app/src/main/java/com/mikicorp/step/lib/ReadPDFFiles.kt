package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfTextExtractor
import com.mikicorp.step.MainActivity
import java.io.File

class ReadPDFFiles {
    var onSuccess: ((result: String) -> Unit)? = {}
    var onError: ((errorText: String) -> Unit)? = {}

    fun openFileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        MainActivity.requestMSFileLauncher.launch(intent)
    }

    @SuppressLint("Range", "Recycle")
    fun handleSelectedFile(uri: Uri) {
        try {
            var parsedText = ""
            val reader = PdfReader(File(uri.path!!).name)
            val n: Int = reader.numberOfPages
            for (i in 0 until n) {
                parsedText = (parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1)
                    .trim()) + "\n" //Extracting the content from the different pages
            }
//            println(parsedText)
            reader.close()
            onSuccess!!.invoke(parsedText)
        } catch (e: Exception) {
            e.printStackTrace()
            onError!!.invoke("Error Open File")
        }//        try {
//            val docFile = context.contentResolver?.openFileDescriptor(uri, "r")
//            val docStream = FileInputStream(docFile?.fileDescriptor)
//            val targetDoc = XWPFDocument(docStream)
//            val wordExtractor = XWPFWordExtractor(targetDoc)
//            docStream.close()
//            onSuccess!!.invoke(wordExtractor.text)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            onError!!.invoke()
//        }
    }
}