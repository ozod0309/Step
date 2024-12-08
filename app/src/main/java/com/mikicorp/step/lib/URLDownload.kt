package com.mikicorp.step.lib

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.mikicorp.step.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object URLDownload {
    internal fun urlDownload(
        context: Context,
        sURL: String,
        requestBody: ArrayList<PostData>? = null,
        onResult: (json: String) -> Unit
    ) {
        if (InternetAvailable.internetAvailable(context)) {
            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                val sData = async { getInetData(sURL, requestBody) }
                val result = sData.await()
                launch(Dispatchers.Main) {
                    onResult(result)
                }
            }
        } else {
            onResult("")
        }
    }


    internal fun getUrlDownload(
        context: Context,
        sURL: String,
        requestBody: ArrayList<PostData>? = arrayListOf(),
        onResult: (json: String) -> Unit
    ) {
        if (InternetAvailable.internetAvailable(context)) {
            CoroutineScope(Dispatchers.Main).launch(Dispatchers.IO) {
                var sGetURl = sURL
                if (requestBody!!.size > 0) {
                    requestBody.forEachIndexed { index, postData ->
                        sGetURl += if (index == 0) "?" else "&"
                        sGetURl = sGetURl + postData.name + '=' + postData.value
                    }
                }
                val sData = async { getInetData(sGetURl) }
                val result = sData.await()
                launch(Dispatchers.Main) {
                    onResult(result)
                }
            }
        } else {
            onResult("")
        }
    }

    fun getInetData(
        sURL: String,
        sRequestBody: ArrayList<PostData>? = null
    ): String {
        val trustAllCerts = arrayOf<TrustManager>(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        var sResult = ""

        val client = OkHttpClient.Builder()
            .sslSocketFactory(
                sslContext.socketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            .hostnameVerifier { _: String?, _: SSLSession? -> true }
            .readTimeout(3, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build()

        var requestBuilder: Request.Builder = Request.Builder()
            .url(sURL)
            .addHeader("Accept", "application/json")
            .addHeader(StepGlobal.ANDROID_ID, MainActivity.androidId)
            .addHeader(StepGlobal.AUTH, "Bearer " + MainActivity.stepUser.stepToken)

        if (sRequestBody != null) {
            val form = FormBody.Builder()
            sRequestBody.forEach {
                form.add(it.name, it.value)
            }
            val body = form.build()
            requestBuilder = requestBuilder.post(body)
        } else {
            requestBuilder = requestBuilder.get()
        }
        val request = requestBuilder.build()

        try {
            val response = client
                .newCall(request)
                .execute()

            sResult = response.body!!.string()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return sResult
    }

    fun uploadFile(url: String, file: File, callback: (Boolean, String?) -> Unit) {
        val fileKey = "file"
        val client = OkHttpClient()

        val trustAllCerts = arrayOf<TrustManager>(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())


        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(fileKey, file.name, file.asRequestBody())
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, response.body?.string())
                } else {
                    callback(false, response.message)
                }
            }
        })
    }
}



object InternetAvailable {
    fun internetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}

data class PostData(
    val name: String,
    val value: String,
)