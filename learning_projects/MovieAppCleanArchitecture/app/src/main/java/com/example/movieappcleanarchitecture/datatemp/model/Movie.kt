package com.example.movieappcleanarchitecture.datatemp.model

/**
 * Business model exposed from the data layer to domain/UI layers.
 * Contains only the fields the app actually needs — trimmed from MovieApiModel.
 * No @SerializedName, no API-specific fields.
 */
data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val voteAverage: Double,
)
