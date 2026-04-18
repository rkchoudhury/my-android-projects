package com.example.movieappcleanarchitecture.presentation.models

import com.example.movieappcleanarchitecture.data.model.Movie

data class MovieState(
    val list: List<Movie> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,
)