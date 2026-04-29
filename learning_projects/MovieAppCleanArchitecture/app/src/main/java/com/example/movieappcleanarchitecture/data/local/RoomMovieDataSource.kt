package com.example.movieappcleanarchitecture.data.local

import android.content.Context
import com.example.movieappcleanarchitecture.data.local.entities.MovieEntity
import com.example.movieappcleanarchitecture.data.model.Movie

class RoomMovieDataSource(context: Context) {
    val movieDao = DatabaseService.getInstance(context).movieDao()

    suspend fun add(movie: Movie)  {
        movieDao.addMovieEntity(MovieEntity.fromMovie(movie))
    }

    suspend fun getAll(): List<Movie> {
        return movieDao.getAllMovieEntities().map { it.toMovie() }
    }
}