package com.example.movieappcleanarchitecture.data.remote.api.impl

import com.example.movieappcleanarchitecture.common.API_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofit: Retrofit =
    Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build()
