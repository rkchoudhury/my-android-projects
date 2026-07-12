package com.example.movieappcleanarchitecture.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.example.movieappcleanarchitecture.data.local.entities.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = REPLACE)
    suspend fun addMovieEntity(movieEntity: MovieEntity)

    // For saving a list of movies at once
    @Insert(onConflict = REPLACE)
    suspend fun addAllMovieEntities(movies: List<MovieEntity>)

    @Query("SELECT * from movie WHERE id = :id")
    suspend fun getMovieEntity(id: Long): MovieEntity?

    @Query("SELECT * from movie")
    suspend fun getAllMovieEntities(): List<MovieEntity>

    @Delete
    suspend fun deleteMovieEntity(movieEntity: MovieEntity)
}