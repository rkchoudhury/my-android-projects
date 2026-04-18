package com.example.movieappcleanarchitecture.data.repository

import com.example.movieappcleanarchitecture.data.Movie

interface MovieRepository {
    suspend fun getPopularMovies(): List<Movie>
}