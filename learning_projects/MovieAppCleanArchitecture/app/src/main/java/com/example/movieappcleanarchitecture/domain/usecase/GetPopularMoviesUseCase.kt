package com.example.movieappcleanarchitecture.domain.usecase

import com.example.movieappcleanarchitecture.data.Movie
import com.example.movieappcleanarchitecture.data.repository.MovieRepository

class GetPopularMoviesUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(): List<Movie> = repository.getPopularMovies()
}