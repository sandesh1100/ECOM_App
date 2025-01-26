package com.acodingsandesh.ecomapp.retro_connection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ORetroClient {
    private const val BASE_URL = "https://klinq.com/"

    val apiService: IApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IApiService::class.java)
    }
}