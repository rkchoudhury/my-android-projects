package com.example.movieappcleanarchitecture.data.repository

import com.example.movieappcleanarchitecture.data.model.Movie

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
}