package com.example.movieappcleanarchitecture.services

import com.example.movieappcleanarchitecture.BuildConfig
import com.example.movieappcleanarchitecture.common.API_BASE_URL
import com.example.movieappcleanarchitecture.data.MovieResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

private const val TOKEN = BuildConfig.TMDB_TOKEN

private val retrofit =
    Retrofit.Builder().baseUrl(API_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build()

val movieService: MovieService = retrofit.create(MovieService::class.java)

interface MovieService {
    @Headers(
        "Accept: application/json",
        "Authorization: Bearer $TOKEN",
    )
    @GET("popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MovieResponse
}