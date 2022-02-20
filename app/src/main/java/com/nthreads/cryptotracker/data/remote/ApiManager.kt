package com.nthreads.cryptotracker.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiManager {

    companion object {
        private val API_VER = "v1"
        private val BASE_URL = "https://api.coindesk.com/$API_VER/bpi/"

        private val logging = run {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }

        private val httpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        private var httpClient = httpClientBuilder.build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <T> newRequest(clazz: Class<T>): T {
            return retrofit.create(clazz)
        }
    }
}