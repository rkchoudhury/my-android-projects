package com.example.movieappcleanarchitecture.data.local

import android.content.Context
import com.example.movieappcleanarchitecture.data.local.entities.MovieEntity
import com.example.movieappcleanarchitecture.data.model.Movie

class MovieLocalDataSource(context: Context) {
    private val movieDao = MovieDatabase.getInstance(context).movieDao()

    suspend fun getAll(): List<Movie> {
        return movieDao.getAllMovieEntities().map { it.toMovie() }
    }

    suspend fun saveAll(movies: List<Movie>) {
        val entities = movies.map { MovieEntity.fromMovie(it) }
        movieDao.addAllMovieEntities(entities)
    }
}