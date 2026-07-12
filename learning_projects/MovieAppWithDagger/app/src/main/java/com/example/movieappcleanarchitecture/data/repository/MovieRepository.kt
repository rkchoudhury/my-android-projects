package com.example.movieappcleanarchitecture.data.repository

import com.example.movieappcleanarchitecture.data.model.Movie

/**
 * Repository interface for movie data.
 * Exposes the clean Movie business model — consumers never see MovieApiModel.
 */
interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
}
