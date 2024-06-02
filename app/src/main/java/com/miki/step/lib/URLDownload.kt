package com.miki.step.lib

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.miki.step.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
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