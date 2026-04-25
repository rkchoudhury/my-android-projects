package com.example.movieappcleanarchitecture.data.remote

import com.example.movieappcleanarchitecture.common.API_BASE_URL
import com.example.movieappcleanarchitecture.data.remote.model.MovieApiModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Remote data source for movie data.
 * Wraps the Retrofit MovieApiService and exposes clean methods.
 *
 * This class owns the Retrofit setup — the rest of the app doesn't
 * need to know about Retrofit, Gson, or any networking details.
 */
class MovieRemoteDataSource {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val movieApiService: MovieApiService =
        retrofit.create(MovieApiService::class.java)

    suspend fun getPopularMovies(): List<MovieApiModel> {
        return movieApiService.getPopularMovies().results
    }
}
