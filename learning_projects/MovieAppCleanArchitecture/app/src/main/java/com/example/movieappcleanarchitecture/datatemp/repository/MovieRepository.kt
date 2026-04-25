package com.example.movieappcleanarchitecture.datatemp.repository

import com.example.movieappcleanarchitecture.datatemp.model.Movie

/**
 * Repository interface for movie data.
 * Exposes the clean Movie business model — consumers never see MovieApiModel.
 */
interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
}
