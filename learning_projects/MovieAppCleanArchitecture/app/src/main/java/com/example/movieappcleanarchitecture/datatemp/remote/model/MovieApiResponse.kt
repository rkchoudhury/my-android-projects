package com.example.movieappcleanarchitecture.datatemp.remote.model

/**
 * Raw API response wrapper — used only for Gson deserialization.
 * Internal to the data layer, NOT exposed to domain/UI layers.
 */
data class MovieApiResponse(
    val results: List<MovieApiModel>
)
