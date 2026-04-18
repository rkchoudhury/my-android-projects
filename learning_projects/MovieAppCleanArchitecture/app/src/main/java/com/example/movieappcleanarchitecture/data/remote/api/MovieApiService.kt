package com.example.movieappcleanarchitecture.data.remote.api

import com.example.movieappcleanarchitecture.BuildConfig
import com.example.movieappcleanarchitecture.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Headers

private const val TOKEN = BuildConfig.TMDB_TOKEN

interface MovieApiService {
    @Headers(
        "Accept: application/json",
        "Authorization: Bearer $TOKEN",
    )
    @GET("popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MovieResponse
}