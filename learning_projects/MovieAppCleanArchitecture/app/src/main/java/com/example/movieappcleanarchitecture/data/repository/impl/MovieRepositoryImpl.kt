package com.example.movieappcleanarchitecture.data.repository.impl

import com.example.movieappcleanarchitecture.data.Movie
import com.example.movieappcleanarchitecture.data.remote.api.MovieApiService
import com.example.movieappcleanarchitecture.data.repository.MovieRepository

class MovieRepositoryImpl(private val api: MovieApiService) : MovieRepository {
    override suspend fun getPopularMovies(): List<Movie> {
        return api.getPopularMovies().results.map { it.toDomain() }
    }
}