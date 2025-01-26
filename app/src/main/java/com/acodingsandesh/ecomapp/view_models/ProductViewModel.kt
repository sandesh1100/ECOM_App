package com.acodingsandesh.ecomapp.view_models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acodingsandesh.ecomapp.models.ProductModel
import com.acodingsandesh.ecomapp.retro_connection.ORetroClient
import com.acodingsandesh.ecomapp.retro_connection.ResponseModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductViewModel: ViewModel() {
    val TAG = ProductViewModel::class.java.name
    val responseLiveData = MutableLiveData<ResponseModel>()
    val errorMsg = MutableLiveData<String>()
    val imagesLiveData = MutableLiveData<ArrayList<String>>()
    val titleLiveData = MutableLiveData<String>()
    val brandNameLiveData = MutableLiveData<String>()
    val priceLiveData = MutableLiveData<String>()
    val skuLiveData = MutableLiveData<String>()
    val descriptionLiveData = MutableLiveData<String>()
    val gson = Gson()
    private lateinit var currentProduct: ProductModel

    fun fetchProductData(){
        try {
            ORetroClient.apiService.getProductDetails().enqueue(object : Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>?,
                    response: Response<ResponseModel>?
                ) {
                    Log.d(TAG, "response received in fetchProductData!")
                    if(response != null){
                        responseLiveData.postValue(response.body())
                    } else {
                        errorMsg.postValue("response is NULL")
                        Log.d(TAG, "response is NULL in fetchProductData!")
                    }
                }

                override fun onFailure(call: Call<ResponseModel>?, t: Throwable?) {
                    Log.d(TAG, "some error in fetchProductData!")
                    errorMsg.postValue(t?.message)
                }

            })
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }
    }

    fun getProduct(pId: String, sId: String, lang: String, store: String){
        try {
            ORetroClient.apiService.getProdDetails(pId, sId, lang, store).enqueue(object : Callback<JsonObject>{
                override fun onResponse(
                    call: Call<JsonObject>?,
                    response: Response<JsonObject>?
                ) {
                    if(response != null){
                        if(response.isSuccessful){
                            Log.d(TAG, "success response")
                            //Log.i(TAG, response.body().toString())
                            val currentJson = JSONObject(gson.toJson(response.body()))
                            val dataJson = currentJson.getJSONObject("data")
                            Log.i(TAG, "Got Data: $dataJson")
                            Log.i(TAG, "Current Product: ${createCurrentProductModel(dataJson) ?: "some error occurred"}")
                            imagesLiveData.postValue(currentProduct.images)
                            titleLiveData.postValue(currentProduct.name)
                            brandNameLiveData.postValue(currentProduct.brandName)
                            priceLiveData.postValue(currentProduct.price)
                            skuLiveData.postValue(currentProduct.sku)
                            descriptionLiveData.postValue(currentProduct.description)
                        } else {
                            Log.d(TAG, "unsuccess response")
                            Log.i(TAG, response.code().toString())
                        }
                    } else {
                        Log.i(TAG, "some error onResponse")
                    }
                }

                override fun onFailure(call: Call<JsonObject>?, t: Throwable?) {
                    Log.d(TAG, "some error in getProducts!")
                    errorMsg.postValue(t?.message)
                }

            })
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
        }
    }

    fun createCurrentProductModel(productJSON: JSONObject): ProductModel?{
        try {
            currentProduct = ProductModel(
                productJSON.optString("id"),
                productJSON.optString("name"),
                convertJAToArrayList(productJSON.optJSONArray("images") ?: JSONArray()),
                productJSON.optString("brand_name"),
                productJSON.optString("sku"),
                convertJAToArrayList(productJSON.optJSONArray("configurable_option") ?: JSONArray()),
                productJSON.optString("description"),
                productJSON.optString("final_price")
            )

            return currentProduct
        } catch (e: Exception) {
            Log.e(TAG, e.stackTraceToString())
            return null
        }
    }

    private fun convertJAToArrayList(jArr: JSONArray): ArrayList<String> {
        val list = ArrayList<String>()
        try {
            var i = 0
            val l = jArr.length()
            while (i < l) {
                list.add(jArr[i].toString())
                i++
            }
        } catch (e: JSONException) {
            Log.e(TAG, e.stackTraceToString())
        }

        return list
    }

    fun showHideDescription(){

    }
}