package com.example.movieappcleanarchitecture.data.repository

import com.example.movieappcleanarchitecture.data.model.Movie
import com.example.movieappcleanarchitecture.data.remote.MovieRemoteDataSource

/**
 * Repository implementation that fetches from the remote data source
 * and maps MovieApiModel → Movie (the business model).
 *
 * This is where the trimming happens — only app-relevant fields are kept.
 */
class MovieRepositoryImpl(
    private val remoteDataSource: MovieRemoteDataSource
) : MovieRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        return remoteDataSource.getPopularMovies().map { apiModel ->
            Movie(
                id = apiModel.id,
                title = apiModel.title,
                originalTitle = apiModel.originalTitle,
                overview = apiModel.overview,
                posterPath = apiModel.posterPath,
                releaseDate = apiModel.releaseDate,
                voteAverage = apiModel.voteAverage,
            )
        }
    }
}
