package com.example.movieappcleanarchitecture.services

import com.example.movieappcleanarchitecture.common.API_BASE_URL
import com.example.movieappcleanarchitecture.data.remote.api.MovieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit =
    Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build()

val movieService: MovieApiService = retrofit.create(MovieApiService::class.java)