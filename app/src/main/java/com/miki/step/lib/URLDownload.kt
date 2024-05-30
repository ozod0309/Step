package com.miki.step.lib

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object URLDownload {
    fun urlDownload(
        context: Context,
        url: String,
        data: java.util.ArrayList<PostData>,
        onResult: (result: String) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
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

            val newBuilder = OkHttpClient.Builder()
            newBuilder.sslSocketFactory(
                sslContext.socketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            newBuilder.hostnameVerifier { _: String?, _: SSLSession? -> true }

            if (InternetAvailable.internetAvailable(context)) {
                val response = async(Dispatchers.IO) {
                    val form = FormBody.Builder()
                    data.forEach {
                        form.add(it.name, it.value)
                    }
                    val requestBody: RequestBody = form
                        .build()
                    val client: OkHttpClient = newBuilder
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build()
                    val request = Request.Builder()
                        .url(url)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(requestBody)
                        .build()
                    try {
                        client.newCall(request).execute().use { result ->
                            if (!result.isSuccessful) throw IOException("Unexpected code $result")
                            return@async result.body!!.string()
                        }
                    } catch (e: Exception) {
                        return@async "{}"
                    }
                }
                onResult(response.await())
            }
        }
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