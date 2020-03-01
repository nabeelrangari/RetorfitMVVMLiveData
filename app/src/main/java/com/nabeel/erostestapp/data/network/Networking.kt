package com.nabeel.erostestapp.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.nabeel.erostestapp.BuildConfig
import com.nabeel.erostestapp.util.App.Companion.appContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Networking {
    private const val NETWORK_CALL_TIMEOUT: Long = 30000
    private const val BASE_URL: String = "https://api.themoviedb.org/"

    fun getApiService(mContext: Context, withHeader: Boolean): NetworkService {
        return getRetrofitInstance(mContext, withHeader);
    }

    private fun getRetrofitInstance(
        mContext: Context,
        withHeader: Boolean
    ): NetworkService {
        return initializeNetworkClient(
            mContext,
            withHeader
        ).create(NetworkService::class.java)
    }

    private fun initializeNetworkClient(
        mContext: Context,
        withHeader: Boolean
    ): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
        client.cache(Cache(appContext.cacheDir, 10 * 1024 * 1024))
        client.connectTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        client.readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        client.writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS)
        client.addInterceptor(NetworkConnectionInterceptor(mContext, withHeader))
        client.addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    level = if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
                })
        client.addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
        return Retrofit.Builder()
            .baseUrl(BASE_URL).client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()
    }
}