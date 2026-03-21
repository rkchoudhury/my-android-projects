package com.example.androidarchitectures.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://restcountries.eu/rest/v2/"

private val retrofit =
    Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build()

val countryService = retrofit.create(CountryService::class.java)

interface CountryService {
    @GET("all?fields=name")
    suspend fun getCountries(): List<Country>
}