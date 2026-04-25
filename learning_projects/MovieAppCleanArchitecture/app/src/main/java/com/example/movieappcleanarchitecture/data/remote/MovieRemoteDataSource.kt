package com.example.movieappcleanarchitecture.data.remote

import com.example.movieappcleanarchitecture.data.remote.model.MovieApiModel

/**
 * Remote data source for movie data.
 * Extends BaseRemoteDataSource to reuse the shared Retrofit instance.
 *
 * This class wraps the MovieApiService and exposes clean methods.
 * The rest of the app doesn't need to know about Retrofit, Gson,
 * or any networking details.
 */
class MovieRemoteDataSource : BaseRemoteDataSource() {

    private val movieApiService = createService(MovieApiService::class.java)

    suspend fun getPopularMovies(): List<MovieApiModel> {
        return movieApiService.getPopularMovies().results
    }
}
