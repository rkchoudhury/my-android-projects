package com.example.movieappcleanarchitecture.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.movieappcleanarchitecture.data.local.dao.MovieDao
import com.example.movieappcleanarchitecture.data.local.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "movie.db"

        private var instance: MovieDatabase? = null

        private fun create(context: Context): MovieDatabase =
            Room.databaseBuilder(context, MovieDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration(false)
                .build()


        fun getInstance(context: Context): MovieDatabase =
            (instance ?: create(context)).also { instance = it }
    }

    abstract fun movieDao(): MovieDao
}