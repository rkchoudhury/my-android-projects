package com.example.movieappcleanarchitecture.data.repository

import com.example.movieappcleanarchitecture.data.local.MovieLocalDataSource
import com.example.movieappcleanarchitecture.data.model.Movie
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource

/**
 * Repository implementation that fetches from the remote data source
 * and maps MovieApiModel → Movie (the business model).
 *
 * This is where the trimming happens — only app-relevant fields are kept.
 */
class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieLocalDataSource
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        try {
            // 1. Fetch from API
            val apiMovies = remoteDataSource.getPopularMovies()

            // 2. Map API models → business models
            val movies = apiMovies.map { eachMovie ->
                Movie(
                    id = eachMovie.id,
                    title = eachMovie.title,
                    originalTitle = eachMovie.originalTitle,
                    overview = eachMovie.overview,
                    posterPath = eachMovie.posterPath,
                    releaseDate = eachMovie.releaseDate,
                    voteAverage = eachMovie.voteAverage,
                )
            }

            // 3. Save to local database (bulk, replaces old data)
            localDataSource.saveAll(movies)

            // 4. Return fresh data
            return movies;
        } catch (e: Exception) {
            // 5. Network failed → fall back to cached local data
            val cachedMovies = localDataSource.getAll()

            // 6. If no cache either, re-throw so ViewModel shows error
            if (cachedMovies.isEmpty()) {
                throw e
            }

            // 7. Return the cached data
            return cachedMovies
        }
    }
}
