package com.outerspace.ip_challenge.network_layer

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "http://ip-api.com/"
const val jsonEndpoint = "/json"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

private interface IPApiService {
    @GET("$jsonEndpoint/{ip_address}")
    suspend fun getIPSchema( @Path("ip_address") ipAddress: String): IPSchema
}

private object IPApi {
    val retrofitService: IPApiService by lazy { retrofit.create(IPApiService::class.java) }
}

class IPClient {
    suspend fun getIPSchema(ip: String): IPSchema {
        return IPApi.retrofitService.getIPSchema(ip)
    }
}
