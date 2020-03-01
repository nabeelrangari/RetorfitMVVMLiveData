package com.nabeel.erostestapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectionInterceptor(private val mContext: Context, private val withHeader: Boolean) :
    Interceptor {
    private val isOnline: Boolean
        get() {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline) {
            throw NoConnectivityException()
        }
        val builder = chain.request().newBuilder()

        if (withHeader) {
            builder.addHeader(
                "Authorization",
                "Bearer" +""
            )
        }

        return chain.proceed(builder.build())
    }

    inner class NoConnectivityException : IOException() {
        override fun getLocalizedMessage(): String {
            return "Network Connection Error!!!"
        }

    }
}