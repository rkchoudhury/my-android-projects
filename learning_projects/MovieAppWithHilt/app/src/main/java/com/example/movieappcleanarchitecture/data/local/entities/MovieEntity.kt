package com.example.movieappcleanarchitecture.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieappcleanarchitecture.data.model.Movie

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val id: Int,

    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
) {
    companion object {
        fun fromMovie(movie: Movie) = MovieEntity(
            movie.id,
            movie.title,
            movie.originalTitle,
            movie.overview,
            movie.posterPath,
            movie.releaseDate,
            movie.voteAverage
        )
    }

    fun toMovie() = Movie(id, title, originalTitle, overview, posterPath, releaseDate, voteAverage)
}


