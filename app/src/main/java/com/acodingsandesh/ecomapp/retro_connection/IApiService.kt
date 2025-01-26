package com.acodingsandesh.ecomapp.retro_connection

import com.acodingsandesh.ecomapp.models.ProductModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {

    @GET("rest/V1/productdetails/6701/253620?lang=en&store=KWD")
    fun getProductDetails(): Call<ResponseModel>

    @GET("rest/V1/productdetails/{productId}/{storeId}")
    fun getProdDetails(
        @Path("productId") productId: String,
        @Path("storeId") storeId: String,
        @Query("lang") lang: String,
        @Query("store") store: String
    ): Call<JsonObject>

}