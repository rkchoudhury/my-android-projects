package com.example.androidarchitectures.model

import com.google.gson.annotations.SerializedName

data class Country (
    @SerializedName("name")
    val countryName: String
)