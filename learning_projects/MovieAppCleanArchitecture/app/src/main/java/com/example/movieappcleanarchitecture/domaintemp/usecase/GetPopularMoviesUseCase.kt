package com.example.movieappcleanarchitecture.domaintemp.usecase

import com.example.movieappcleanarchitecture.datatemp.model.Movie
import com.example.movieappcleanarchitecture.datatemp.repository.MovieRepository


/**
 * Use case that fetches popular movies.
 *
 * Per Google's guidelines, each use case:
 * - Has a single responsibility (one action)
 * - Uses operator fun invoke() so it can be called like a function
 * - Is named as: verb in present tense + noun/what (optional) + UseCase
 *   e.g., GetPopularMoviesUseCase
 * - Depends on repository from the data layer
 *
 * Ref: https://developer.android.com/topic/architecture/domain-layer
 */
class GetPopularMoviesUseCase(private val movieRepository: MovieRepository) {
    suspend operator fun invoke(): List<Movie> = movieRepository.getPopularMovies()
}
