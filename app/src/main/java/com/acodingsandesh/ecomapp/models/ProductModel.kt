package com.acodingsandesh.ecomapp.models

data class ProductModel(
    val id: String,
    val name: String,
    val images: ArrayList<String>,
    val brandName: String,
    val sku: String,
    val configurableOption: ArrayList<String>?,
    val description: String,
    val price: String
)