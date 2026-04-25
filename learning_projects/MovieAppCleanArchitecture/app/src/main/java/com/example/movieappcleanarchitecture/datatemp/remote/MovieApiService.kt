package com.example.movieappcleanarchitecture.datatemp.remote

import com.example.movieappcleanarchitecture.BuildConfig
import com.example.movieappcleanarchitecture.datatemp.remote.model.MovieApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers

private const val TOKEN = BuildConfig.TMDB_TOKEN

/**
 * Retrofit interface for the TMDB API.
 * This is an implementation detail of MovieRemoteDataSource.
 */
interface MovieApiService {
    @Headers(
        "Accept: application/json",
        "Authorization: Bearer $TOKEN",
    )
    @GET("popular?language=en-US&page=1")
    suspend fun getPopularMovies(): MovieApiResponse
}
