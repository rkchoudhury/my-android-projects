package com.example.movieappcleanarchitecture.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Raw API response model — maps directly to the TMDB JSON structure.
 * Contains ALL fields from the API. This is internal to the data layer
 * and should NOT be exposed to domain/UI layers.
 */
data class MovieApiModel(
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)
