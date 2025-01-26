package com.acodingsandesh.ecomapp.retro_connection

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class ResponseModel(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: JSONObject
)
