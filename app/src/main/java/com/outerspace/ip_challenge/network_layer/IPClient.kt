package com.outerspace.ip_challenge.network_layer

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "http://ip-api.com/"
const val jsonEndpoint = "/json"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val loggingInterceptor = HttpLoggingInterceptor(
    logger = HttpLoggingInterceptor.Logger {
            message -> Log.d("TEST", "Logger message: $message")
    }).apply { level = HttpLoggingInterceptor.Level.BASIC }

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .build()

private interface IPApiService {
    @GET("$jsonEndpoint/{ip_address}")
    suspend fun fetchIPSchema(@Path("ip_address") ipAddress: String): IPSchema
}

private object IPApi {
    val retrofitService: IPApiService by lazy {
        retrofit.create(IPApiService::class.java)
    }
}

class IPClient {
    suspend fun fetchIPSchema(ip: String): IPSchema {
        return IPApi.retrofitService.fetchIPSchema(ip)
    }
}
